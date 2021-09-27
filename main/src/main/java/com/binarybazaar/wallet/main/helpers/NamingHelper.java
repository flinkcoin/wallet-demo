package com.binarybazaar.wallet.main.helpers;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamingHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamingHelper.class);

    public static void bind(Context context, String name, Object object) {
        try {
            context.bind(name, object);
        } catch (NamingException ex) {
            LOGGER.error("JNDI", ex);
        }
    }

    public static void unbind(Context context, String name) {
        try {
            context.unbind(name);
        } catch (NamingException ex) {
            LOGGER.error("JNDI", ex);
        }
    }

    public static Context initContext(String namePath) {
        Context context = null;
        try {
            Deque<String> names = new ArrayDeque<>(Arrays.asList(namePath.split("/")));
            context = createContext(new InitialContext(), names);
        } catch (NamingException ex) {
            LOGGER.error("Could not init context!", ex);
        }
        return context;
    }

    private static Context createContext(Context context, Deque<String> subcontextNames) throws NamingException {
        if (subcontextNames.isEmpty()) {
            return context;
        }
        String firstSubcontextName = subcontextNames.pollFirst();
        Context subcontext = doContextLookup(context, firstSubcontextName, Context.class);
        if (subcontext == null) {
            subcontext = context.createSubcontext(firstSubcontextName);
        }
        return createContext(subcontext, subcontextNames);
    }

    private static <T> T doContextLookup(Context context, String name, Class<? extends T> type) {
        try {
            Object boundObject = context.lookup(name);
            if (boundObject == null) {
                return null;
            }
            return type.cast(boundObject);
        } catch (NamingException ex) {
            return null;
        }
    }

}
