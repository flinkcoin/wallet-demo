package org.flinkcoin.wallet.main.helpers;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowableBiConsumer<T, U> extends BiConsumer<T, U> {

    @Override
    default void accept(final T t, final U u) {
        try {
            acceptThrowsException(t, u);
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    void acceptThrowsException(final T t, final U u) throws Exception;
}
