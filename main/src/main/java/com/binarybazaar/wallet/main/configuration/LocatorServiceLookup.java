package com.binarybazaar.wallet.main.configuration;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import org.glassfish.hk2.api.ServiceLocator;

@Provider
public class LocatorServiceLookup implements Feature {

    public static ServiceLocator SERVICE_LOCATOR;

    private final ServiceLocator scopedLocator;

    @Inject
    private LocatorServiceLookup(ServiceLocator scopedLocator) {
        this.scopedLocator = scopedLocator;
    }

    @Override
    public boolean configure(FeatureContext context) {
        SERVICE_LOCATOR = this.scopedLocator; // this would set our member locator variable
        return true;
    }

}
