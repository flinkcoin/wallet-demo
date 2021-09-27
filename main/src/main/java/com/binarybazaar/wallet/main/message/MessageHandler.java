package com.binarybazaar.wallet.main.message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final Map<String, Session> accountSessions;

    public MessageHandler() {
        this.accountSessions = new ConcurrentHashMap<>();
    }

    public void openClient(Session session, String account) {
        accountSessions.put(account, session);
    }

    public void closeClient(String account) {
        accountSessions.remove(account);
    }

    public void receiveTextFromClient(Session session, String account, String message) {

    }

    public void sendTextToClient(String account, String text) throws IOException {
        Session session = accountSessions.get(account);

        if (session == null) {
            return;
        }

        session.getRemote().sendString(text);
    }

}
