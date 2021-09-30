package org.flinkcoin.wallet.main.resources;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public abstract class ResourceBase {

    @Context
    private SecurityContext securityContext;

    public ResourceBase() {
    }

    protected SecurityContext getSecurityContext() {
        return securityContext;
    }

}
