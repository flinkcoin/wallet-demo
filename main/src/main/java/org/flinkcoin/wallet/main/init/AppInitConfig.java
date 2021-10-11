package org.flinkcoin.wallet.main.init;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class AppInitConfig implements Feature {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppInitConfig.class);

    private final AppInit[] appInitServices;

    @Inject
    public AppInitConfig() {
        this.appInitServices = new AppInit[]{
        
        };
    }

    @Override
    public boolean configure(FeatureContext context) {
        boolean enabled = false;
        try {
//            executeAllInitServices();
            enabled = true;
        } catch (IllegalStateException ex) {
            LOGGER.error("Some init problem!", ex);
        }
        return enabled;
    }

//    private void executeAllInitServices() {
//        for (AppInit appInitService : appInitServices) {
//            time(() -> {
//                appInitService.exec();
//            }, appInitService.getName());
//        }
//    }

//    private void time(ThrowableRunnable runnable, String serviceName) {
//        LOGGER.info("Start: {}", serviceName);
//        StopWatch sw = StopWatch.createStarted();
//        runnable.run();
//        sw.stop();
//        LOGGER.info("End: {}, TIME: {} ms", serviceName, sw.getTime(TimeUnit.MILLISECONDS));
//    }

}
