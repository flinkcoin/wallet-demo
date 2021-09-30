package org.flinkcoin.wallet.main.persistence;

import org.flinkcoin.wallet.main.helpers.NamingHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceIndex {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceIndex.class);

    private static final String JNDI_MAIN_DATASOURCE = "main";
    private static final String JNDI_CONTEXT = "java:comp/env/jdbc/ds";
    private static final int MAIN_POOL_SIZE = 10;

    private final ConcurrentHashMap<String, HikariDataSource> dataSourceMap;
    private final Context jdbcCtx;

    public DataSourceIndex() {
        this.dataSourceMap = new ConcurrentHashMap<>();
        this.jdbcCtx = NamingHelper.initContext(JNDI_CONTEXT);
    }

    public static String getMainJndiPath() {
        return JNDI_CONTEXT + "/" + JNDI_MAIN_DATASOURCE;
    }

    private String getMainJndiName() {
        return JNDI_MAIN_DATASOURCE;
    }

    private void bind(String jndiName, HikariDataSource ds) {
        NamingHelper.bind(this.jdbcCtx, jndiName, ds);
    }

    private void unbind(String jndiName) {
        NamingHelper.unbind(this.jdbcCtx, jndiName);
    }

    public void addMainDatabase(String serverName, int portNumber, String databaseName, String username, String password) {
        String jndiName = getMainJndiName();
        addDataSource(jndiName, serverName, portNumber, databaseName, username, password, MAIN_POOL_SIZE);
    }

    private void addDataSource(String jndiName, String serverName, int portNumber, String databaseName, String username, String password, int poolSize) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        config.addDataSourceProperty("databaseName", databaseName);
        config.addDataSourceProperty("portNumber", portNumber);
        config.addDataSourceProperty("serverName", serverName);
        config.setMaximumPoolSize(poolSize);

        HikariDataSource ds = new HikariDataSource(config);
        migrateDb(ds);

        bind(jndiName, ds);

        try {
            InitialContext.doLookup(JNDI_CONTEXT + "/" + jndiName);
        } catch (NameNotFoundException ex) {
            LOGGER.error("Naming exception!", ex);
        } catch (NamingException ex) {
            LOGGER.error("Naming exception!", ex);
        }

        dataSourceMap.put(jndiName, ds);
    }

    private void migrateDb(HikariDataSource ds) {
        Flyway flyway = Flyway
                .configure()
                .dataSource(ds)
                .load();

        flyway.migrate();
    }

    public Optional<HikariDataSource> getDataSource(String jndiName) {
        return Optional.ofNullable(dataSourceMap.get(jndiName));
    }

    protected void clear() {
        clearDataSourceMap();
    }

    private void clearDataSourceMap() {
        for (Map.Entry<String, HikariDataSource> entry : dataSourceMap.entrySet()) {
            unbind(entry.getKey());
            HikariDataSource ds = entry.getValue();
            ds.close();
            ds.shutdown();
        }
        dataSourceMap.clear();
    }

}
