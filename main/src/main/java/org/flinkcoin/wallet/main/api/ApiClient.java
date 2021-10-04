package org.flinkcoin.wallet.main.api;

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

import org.flinkcoin.wallet.main.configuration.Config;
import org.flinkcoin.data.proto.api.AccountServiceGrpc;
import org.flinkcoin.data.proto.api.AccountServiceGrpc.AccountServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ApiClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);

    private final ManagedChannel channel;

    private final AccountServiceBlockingStub accountServiceBlockingStub;

    @Inject
    public ApiClient() {
        this.channel = ManagedChannelBuilder.forAddress("localhost", Config.get().apiPort())
                .usePlaintext()
                .build();

        this.accountServiceBlockingStub = AccountServiceGrpc.newBlockingStub(channel);
    }

    public AccountServiceBlockingStub getAccountServiceStub() {
        return accountServiceBlockingStub;
    }

    @Override
    public void close() throws Exception {
        channel.shutdown();
        channel.awaitTermination(10, TimeUnit.SECONDS);
    }
}
