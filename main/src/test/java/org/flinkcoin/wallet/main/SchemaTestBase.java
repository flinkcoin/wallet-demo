package org.flinkcoin.wallet.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SchemaTestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaTestBase.class);
    private EntityManagerFactory emFactory;
    private EntityTransaction entityTransaction;
    private EntityManager em;

    protected boolean wipeDbAfterTest() {
        return true;
    }

    protected String getPersistenceUnitName() {
        return "testPersistenceUnit";
    }

    @BeforeEach
    public void setUp() throws Exception {
        try {
            emFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
        } catch (javax.persistence.PersistenceException ex) {
            String strErr;
            try (final StringWriter sw = new StringWriter()) {
                try (final PrintWriter pw = new PrintWriter(sw, true)) {
                    ex.printStackTrace(pw);
                    strErr = sw.getBuffer().toString();
                }
            }
            throw new Exception("Error creating EntityManagerFactory:" + strErr, ex);
        }
        if (emFactory == null) {
            throw new Exception("emFactory null. Error creating EntityManagerFactory.");
        }
        em = emFactory.createEntityManager();
        entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
        } catch (javax.persistence.PersistenceException ex) {
            throw new Exception("Error connecting to the database.", ex);
        }
        wipeSchema(em);
        entityTransaction.commit();
        em.close();
        emFactory.close();

        /*
         *Prepare for testing
         */
//        Properties props = new Properties();
//        props.put("hibernate.hbm2ddl.auto", "update");
        emFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
        em = emFactory.createEntityManager();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (em == null || emFactory == null) {
            return;
        }
        em.close();
        emFactory.close();

        /*
         * Clean finally
         */
        emFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
        if (emFactory == null) {
            LOGGER.error("(could be caused by previous error...)Error tearing down test. Error creating EntityManagerFactory.");
            em.close();
            emFactory.close();
            return;
        }
        em = emFactory.createEntityManager();
        entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
        } catch (javax.persistence.PersistenceException ex) {
            LOGGER.error("could be caused by previous error...)Error tearing down test. Error connecting to the database.{}", ex);
            ex.printStackTrace();
            em.close();
            emFactory.close();
            return;
        }
        wipeSchema(em);
        entityTransaction.commit();
        em.close();
        emFactory.close();
    }

    protected void compareWithApgDiff() throws Exception {

        String connectionString = (String) emFactory.getProperties().get("javax.persistence.jdbc.url");
        String hostname = connectionString.split("/")[2].split(":")[0];
        String databaseName = connectionString.split("/")[3];
        String username = (String) emFactory.getProperties().get("javax.persistence.jdbc.user");
        String password = (String) emFactory.getProperties().get("javax.persistence.jdbc.password");
        String databaseNameFromScripts = databaseName.replace("_helper", "");

        runExtraMigrations(hostname, databaseName, username, password);

        runAndWait(new String[]{"pg_dump", "-U", username, "-h", hostname, "-f", "from_scripts.sql", "-s", databaseNameFromScripts},
                "PGPASSWORD=" + password);

        runAndWait(new String[]{"pg_dump", "-U", username, "-h", hostname, "-f", "from_model.sql", "-s", databaseName},
                "PGPASSWORD=" + password);

        Path apgDiffPath = Paths.get("../apgdiff/apgdiff-2.4.jar");

        String sqlDiff = runAndWait(new String[]{"java", "-jar", apgDiffPath.toAbsolutePath().toString(), "--ignore-start-with", "from_scripts.sql", "from_model.sql"});

        new File("from_model.sql").delete();
        new File("from_scripts.sql").delete();
        Assertions.assertTrue(sqlDiff.isEmpty(), "non empty difference detected by apgdiff: " + sqlDiff);
    }

    private static String getStringFromInputStream(InputStream is) {
        StringBuilder sb = new StringBuilder();

        String line;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException ex) {
            LOGGER.error(null, ex);
        }
        return sb.toString();
    }

    private static String runAndWait(String[] command, String... envp) throws Exception {
        Process p = Runtime.getRuntime().exec(command, envp);
        /* This has to be before waitFor, otherwise waitFor blocks on larger input streams */
        String output = getStringFromInputStream(p.getInputStream());
        if (p.waitFor() != 0) {
            String errorString = getStringFromInputStream(p.getErrorStream());
            throw new IOException("External command error: " + errorString);
        }
        return output;
    }

    private void runExtraMigrations(String hostname, String databaseName, String username,
            String password) throws Exception {
        LOGGER.info("Running migrations after script.");
        String resourceName = "after-migrations.sql";
        File target = null;
        try {
            target = createTempFile(resourceName);
            copyResourceToFile(resourceName, getClass(), target);
            runAndWait(new String[]{"psql", "-e", "-U", username, "-h", hostname, "-d", databaseName, "-f", target.getAbsolutePath()},
                    "PGPASSWORD=" + password, "PATH=" + System.getenv("PATH"));
        } finally {
            if (target != null) {
                target.delete();
            }
        }
    }

    public static void copyResourceToFile(final String resourceFileName, Class classForClassLoader, File target) {
        LOGGER.debug(resourceFileName);
        try (InputStream input = classForClassLoader.getClassLoader().getResourceAsStream(resourceFileName);
                FileOutputStream fos = new FileOutputStream(target)) {
            if (input == null) {
                throw new RuntimeException(String.format("could not load resource \"%s\". does it exist?", resourceFileName));
            }
            LOGGER.debug("{} -> {}", resourceFileName, target.getAbsolutePath());
            byte[] buf = new byte[4096];
            int read;
            while ((read = input.read(buf, 0, buf.length)) != -1) {
                fos.write(buf, 0, read);
            }
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Error loading \"%s\".", resourceFileName), ex);
        }
    }

    public static File createTempFile(final String resourceName) {
        return new File("tempFileName");
    }

    private void wipeSchema(EntityManager em) throws Exception {
        LOGGER.info("Wiping helper DB...");

        Query queryDrop = em.createNativeQuery("SELECT drop_tables()");
        queryDrop.getSingleResult();
    }
}
