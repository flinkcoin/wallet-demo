package com.binarybazaar.wallet.main.persistence;

import com.binarybazaar.wallet.main.helpers.ThrowableBiConsumer;
import com.binarybazaar.wallet.main.helpers.ThrowableBiFunction;
import com.binarybazaar.wallet.main.helpers.ThrowableConsumer;
import com.binarybazaar.wallet.main.helpers.ThrowableFunction;
import com.binarybazaar.wallet.main.helpers.ThrowableRunnable;
import com.binarybazaar.wallet.main.helpers.ThrowableSupplier;
import static com.binarybazaar.wallet.main.helpers.TransactionHelper.SHARD_SEARCH_PATH;
import static com.binarybazaar.wallet.main.helpers.TransactionHelper.executeInTransactionCloseEm;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import javax.persistence.EntityManager;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TransactionManager implements AutoCloseable {

    private EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> threadLocal;

    public TransactionManager(String persistenceUnitName, String jndiPath) {
        this.threadLocal = new ThreadLocal<>();

        initEntityManagerFactory(persistenceUnitName, jndiPath);
    }

    private void initEntityManagerFactory(String persistenceUnitName, String jndiPath) {
        Map properties = new HashMap();
        properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, jndiPath);
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
    }

    /**
     * Use only if really needed!
     *
     * @return
     */
    @Deprecated
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }

    public void newGlobalTransaction(String schemaName, ThrowableConsumer<EntityManager> consumer) {
        newGlobalTransaction(schemaName, (em) -> {
            consumer.accept(em);
            return Optional.empty();
        });
    }

    public void newGlobalTransaction(String schemaName, ThrowableBiConsumer<EntityManager, JPAQueryFactory> consumer) {
        newGlobalTransaction(schemaName, (em) -> {
            consumer.accept(em, new JPAQueryFactory(em));
            return Optional.empty();
        });
    }

    public <T> T newGlobalTransaction(String schemaName, ThrowableSupplier<T> supplier) {
        T val = newGlobalTransaction(schemaName, (em) -> {
            return supplier.get();
        });
        return val;
    }

    public void newGlobalTransaction(String schemaName, ThrowableRunnable runnable) {
        newGlobalTransaction(schemaName, (em) -> {
            runnable.run();
            return Optional.empty();
        });
    }

    public <T> T newGlobalTransaction(String schemaName, ThrowableFunction<EntityManager, T> function) {
        EntityManager entityManager = threadLocal.get();

        if (entityManager != null) {
            throw new IllegalThreadStateException("There is already a global transaction!");
        }

        entityManager = createEntityManager(schemaName);

        threadLocal.set(entityManager);
        T returnValue = null;
        try {
            returnValue = executeInTransactionCloseEm(entityManager, function);
        } finally {
            threadLocal.remove();
        }

        return returnValue;
    }

    public <R> R newGlobalTransaction(String schemaName, ThrowableBiFunction<EntityManager, JPAQueryFactory, R> function) {
        return newGlobalTransaction(schemaName, (em) -> {
            JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            return function.applyThrowsException(em, queryFactory);
        });
    }

    public void newLocalTransaction(String schemaName, ThrowableConsumer<EntityManager> consumer) {
        newLocalTransaction(schemaName, (em) -> {
            consumer.accept(em);
            return Optional.empty();
        });
    }

    public void newLocalTransaction(String schemaName, ThrowableBiConsumer<EntityManager, JPAQueryFactory> consumer) {
        newLocalTransaction(schemaName, (em) -> {
            consumer.accept(em, new JPAQueryFactory(em));
            return Optional.empty();
        });
    }

    public <T> T newLocalTransaction(String schemaName, ThrowableFunction<EntityManager, T> function) {

        EntityManager em = createEntityManager(schemaName);

        return executeInTransactionCloseEm(em, function);
    }

    public <T> T newLocalTransaction(String schemaName, ThrowableBiFunction<EntityManager, JPAQueryFactory, T> function) {
        return newLocalTransaction(schemaName, (em) -> {
            JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            return function.applyThrowsException(em, queryFactory);
        });
    }

    public EntityManager getGlobalEntityManager() {
        EntityManager entityManager = threadLocal.get();

        if (entityManager == null) {
            throw new IllegalThreadStateException("No global transaction created!");
        }
        return entityManager;
    }

    public JPAQueryFactory getGlobalJPAQueryFactory() {
        return new JPAQueryFactory(getGlobalEntityManager());
    }

    private EntityManager createEntityManager(String schemaName) {

        if (schemaName == null) {
            return entityManagerFactory.createEntityManager();
        }

        Map properties = new HashMap();
        properties.put(SHARD_SEARCH_PATH, schemaName);
        return entityManagerFactory.createEntityManager(properties);
    }

}