package org.flinkcoin.wallet.main.helpers;

/*-
 * #%L
 * Wallet - Main
 * %%
 * Copyright (C) 2021 Flink Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
