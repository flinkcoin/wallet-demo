package com.binarybazaar.wallet.main.message;

import java.net.URI;
import javax.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageRouterAdapter extends WebSocketAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRouterAdapter.class);
    private Session session;
    private String account;

    @Inject
    private MessageHandler mh;

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        URI uri = session.getUpgradeRequest().getRequestURI();
        this.account = uri.getPath().replace("/message/", "");

        mh.openClient(session, account);
        super.onWebSocketConnect(session);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        mh.closeClient(account);
        super.onWebSocketClose(statusCode, reason);
    }

    @Override
    public void onWebSocketText(String message) {
        LOGGER.debug("received Message TEXT : {}", message);

        try {
            mh.receiveTextFromClient(session, account, message);
        } catch (Exception ex) {
            LOGGER.error("Problem handling message!", ex);
        }
        super.onWebSocketText(message);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        LOGGER.error("Not implemented");
        super.onWebSocketBinary(payload, offset, len);
    }

}
