package org.flinkcoin.wallet.main.configuration;

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
