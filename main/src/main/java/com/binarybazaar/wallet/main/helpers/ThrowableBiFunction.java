package com.binarybazaar.wallet.main.helpers;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> extends BiFunction<T, U, R> {

    @Override
    default R apply(final T t, final U u) {
        try {
            return applyThrowsException(t, u);
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    R applyThrowsException(final T t, final U u) throws Exception;
}
