package org.flinkcoin.wallet.main;

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
import org.flinkcoin.wallet.main.message.MessageServlet;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.io.IOException;
import java.util.logging.Level;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.slf4j.Logger;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public abstract class MainApplicationBase extends AbstractBinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplicationBase.class);

    private static final Class CLASS = MainApplicationBase.class;


    protected static void initBridgeLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    protected static void initRequestLogging(ResourceConfig rc, java.util.logging.Logger bridgeLogger) {
        if (!Config.get().httpLogging()) {
            return;
        }
        rc.register(new LoggingFeature(bridgeLogger, Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
    }

    protected static void initMessaging(ServletContextHandler context) throws IOException {
        ServletHolder servletHolder = new ServletHolder(MessageServlet.class);
        context.addServlet(servletHolder, "/message/*");
    }

    protected static ScanResult scanApp() {
        return new ClassGraph()
                .enableAllInfo() // Scan classes, methods, fields, annotations
                .whitelistPackages("org.flinkcoin.wallet") // Scan com.xyz and subpackages (omit to scan all packages)
                .scan();
    }

    protected static void bindResources(ScanResult scanResult, ResourceConfig rc) {
        for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(Path.class.getName())) {
            rc.register(routeClassInfo.loadClass());
        }
    }

    protected static void bindProviders(ScanResult scanResult, ResourceConfig rc) {
        for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(Provider.class.getName())) {
            rc.register(routeClassInfo.loadClass());
        }
    }

}
