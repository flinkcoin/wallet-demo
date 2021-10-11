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
package org.flinkcoin.wallet.main.message;

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
