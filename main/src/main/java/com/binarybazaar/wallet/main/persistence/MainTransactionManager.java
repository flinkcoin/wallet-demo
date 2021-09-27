package com.binarybazaar.wallet.main.persistence;

import com.binarybazaar.wallet.main.helpers.ThrowableBiConsumer;
import com.binarybazaar.wallet.main.helpers.ThrowableBiFunction;
import com.binarybazaar.wallet.main.helpers.ThrowableConsumer;
import com.binarybazaar.wallet.main.helpers.ThrowableFunction;
import com.binarybazaar.wallet.main.helpers.ThrowableRunnable;
import com.binarybazaar.wallet.main.helpers.ThrowableSupplier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;


public class MainTransactionManager extends TransactionManager {

    public static final String MAIN_PERSTISTINCE_UNIT = "mainPersistenceUnit";
    private static final String SCHEMA_NAME = "public";

    public MainTransactionManager(String jndiPath) {
        super(MAIN_PERSTISTINCE_UNIT, jndiPath);
    }

    public void newGlobalTransaction(ThrowableConsumer<EntityManager> consumer) {
        newGlobalTransaction(SCHEMA_NAME, consumer);
    }

    public void newGlobalTransaction(ThrowableBiConsumer<EntityManager, JPAQueryFactory> consumer) {
        newGlobalTransaction(SCHEMA_NAME, consumer);
    }

    public <T> T newGlobalTransaction(ThrowableSupplier<T> supplier) {
        return newGlobalTransaction(SCHEMA_NAME, supplier);
    }

    public void newGlobalTransaction(ThrowableRunnable runnable) {
        newGlobalTransaction(SCHEMA_NAME, runnable);
    }

    public <T> T newGlobalTransaction(ThrowableFunction<EntityManager, T> function) {
        return newGlobalTransaction(SCHEMA_NAME, function);
    }

    public <R> R newGlobalTransaction(ThrowableBiFunction<EntityManager, JPAQueryFactory, R> function) {
        return newGlobalTransaction(SCHEMA_NAME, function);
    }

    public void newLocalTransaction(ThrowableConsumer<EntityManager> consumer) {
        newLocalTransaction(SCHEMA_NAME, consumer);
    }

    public void newLocalTransaction(ThrowableBiConsumer<EntityManager, JPAQueryFactory> consumer) {
        newLocalTransaction(SCHEMA_NAME, consumer);
    }

    public <T> T newLocalTransaction(ThrowableFunction<EntityManager, T> function) {
        return newLocalTransaction(SCHEMA_NAME, function);
    }

    public <T> T newLocalTransaction(ThrowableBiFunction<EntityManager, JPAQueryFactory, T> function) {
        return newLocalTransaction(SCHEMA_NAME, function);
    }
}
