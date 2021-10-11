/*
 * Copyright © 2021 Flink Foundation (info@flinkcoin.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flinkcoin.wallet.main.persistence;

import org.flinkcoin.wallet.main.helpers.ThrowableBiConsumer;
import org.flinkcoin.wallet.main.helpers.ThrowableBiFunction;
import org.flinkcoin.wallet.main.helpers.ThrowableConsumer;
import org.flinkcoin.wallet.main.helpers.ThrowableFunction;
import org.flinkcoin.wallet.main.helpers.ThrowableRunnable;
import org.flinkcoin.wallet.main.helpers.ThrowableSupplier;
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
