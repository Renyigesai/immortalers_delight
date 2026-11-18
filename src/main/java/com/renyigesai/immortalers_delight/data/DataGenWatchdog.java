package com.renyigesai.immortalers_delight.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataGenWatchdog {

    private static final Logger LOGGER = LoggerFactory.getLogger("DataGenWatchdog");
    private static final long POLL_INTERVAL_MS = 500L;
    private static final long POST_COMPLETE_DELAY_MS = 3000L;
    private static volatile boolean generationComplete;

    private DataGenWatchdog() {}

    public static void start(long maxWaitMs) {
        Thread watchdog = new Thread(() -> run(maxWaitMs), "datagen-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        LOGGER.info("DataGenWatchdog started; timeout {} ms", maxWaitMs);
    }

    public static void markComplete() {
        generationComplete = true;
    }

    private static void run(long maxWaitMs) {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < maxWaitMs) {
            try {
                Thread.sleep(POLL_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            if (generationComplete) {
                LOGGER.info("All data providers completed; exiting shortly");
                sleepQuietly(POST_COMPLETE_DELAY_MS);
                System.exit(0);
                return;
            }
        }

        LOGGER.error("Data generation timed out after {} ms", maxWaitMs);
        System.exit(1);
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
