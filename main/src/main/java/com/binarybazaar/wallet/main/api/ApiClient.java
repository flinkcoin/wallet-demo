package com.binarybazaar.wallet.main.api;

import com.binarybazaar.wallet.main.configuration.Config;
import com.flick.data.proto.api.AccountServiceGrpc;
import com.flick.data.proto.api.AccountServiceGrpc.AccountServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

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
