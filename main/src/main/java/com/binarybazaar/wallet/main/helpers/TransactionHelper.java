package com.binarybazaar.wallet.main.helpers;

import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionHelper.class);

    public static final String SHARD_SEARCH_PATH = "exchange.database.shard.search.path";

    public static <T> T executeInTransactionCloseEm(EntityManager em, ThrowableFunction<EntityManager, T> f) {
        T result;
        try {
            result = TransactionHelper.executeInTransaction(em, f);
        } finally {
            em.close();
        }
        return result;
    }

    public static <T> T executeInTransactionCloseEm(Optional<EntityManager> em, ThrowableFunction<EntityManager, T> f) {

        if (!em.isPresent()) {
            LOGGER.error("EntityManager is empty!");
            return null;
        }

        return executeInTransactionCloseEm(em.get(), f);
    }

    public static <T> T executeInTransaction(EntityManager em, ThrowableFunction<EntityManager, T> f) {
        boolean transactionSuccess = false;

        EntityTransaction tx = em.getTransaction();
        T apply;

        try {
            tx.begin();
            verifyShard(em);
            apply = f.apply(em);
            transactionSuccess = true;
        } finally {
            TransactionHelper.commit(tx, transactionSuccess, "executeInTransaction");
        }

        return apply;
    }

    private static void verifyShard(EntityManager em) {

        Map<String, Object> p = em.getProperties();

        if (p == null) {
            return;
        }

        String schemaName = (String) p.get(SHARD_SEARCH_PATH);

        if (schemaName == null) {
            return;
        }

        em.createNativeQuery("SET search_path TO " + schemaName).executeUpdate();
    }

    public static void commit(EntityTransaction tx, boolean transactionSuccess) {
        commit(tx, transactionSuccess, null);
    }

    public static void commit(EntityTransaction tx, boolean transactionSuccess, String source) {
        if (transactionSuccess) {
            tx.commit();
        } else {
            tx.rollback();
        }
    }
}
