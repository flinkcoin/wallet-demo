package com.binarybazaar.wallet.main.helpers;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowableSupplier<T> extends Supplier<T> {

    @Override
    default T get() {
        try {
            return getThrowsException();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    T getThrowsException() throws Exception;
}
