package org.flinkcoin.wallet.main.helpers;

@FunctionalInterface
public interface ThrowableRunnable extends Runnable {

    @Override
    default void run() {
        try {
            runThrowsException();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    void runThrowsException() throws Exception;
}
