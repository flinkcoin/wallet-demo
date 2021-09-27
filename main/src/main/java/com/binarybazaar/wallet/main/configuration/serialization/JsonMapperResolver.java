package com.binarybazaar.wallet.main.configuration.serialization;

import com.binarybazaar.wallet.helper.JsonObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JsonMapperResolver implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return JsonObjectMapper.getMapper();
    }
}
