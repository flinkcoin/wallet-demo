package org.flinkcoin.wallet.main.message;

import org.flinkcoin.wallet.main.message.MessageDtl.InfoResponse;
import org.flinkcoin.data.proto.api.Api.InfoReq;
import org.flinkcoin.helper.JsonObjectMapper;
import org.flinkcoin.helper.helpers.Base32Helper;
import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.flinkcoin.wallet.main.api.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class MessageForwarder implements Runnable, Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageForwarder.class);

    private final MessageHandler messageHandler;
    private final ApiClient apiClient;
    private Thread t;

    @Inject
    public MessageForwarder(MessageHandler messageHandler, ApiClient apiClient) {
        this.messageHandler = messageHandler;
        this.apiClient = apiClient;
    }

    private void listen() {
        InfoReq infoReq = InfoReq.newBuilder().setId(0).build();

        apiClient.getAccountServiceStub().receiveInfos(infoReq)
                .forEachRemaining(x -> {

                    try {
                        String account = Base32Helper.encode(x.getAccountId().toByteArray());

                        InfoResponse infoResponse = new InfoResponse(x);

                        String json = JsonObjectMapper.getMapper().writeValueAsString(infoResponse);

                        messageHandler.sendTextToClient(account, json);

                        LOGGER.error("Problems!");

                    } catch (Exception ex) {
                        LOGGER.error("IO exception!", ex);
                    }
                });

        int x = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                listen();
            } catch (Exception ex) {
                LOGGER.error("Problem listening!", ex);
            }
        }
    }

    @Override
    public boolean configure(FeatureContext fc) {
        t = new Thread(this);
        t.start();
        return true;
    }
}
