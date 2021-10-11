package org.flinkcoin.wallet.main.api;

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
