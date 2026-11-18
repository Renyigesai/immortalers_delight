package com.renyigesai.immortalers_delight.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DataGenWatchdog {

    private static final Logger LOGGER = LoggerFactory.getLogger("DataGenWatchdog");
    private static final long POLL_INTERVAL_MS = 500L;

    /** 数据生成完成后的等待时间（毫秒），给异步写入留时间 */
    private static final long POST_COMPLETE_DELAY_MS = 3000L;

    private DataGenWatchdog() {}

    /**
     * 启动看门狗。
     *
     * @param maxWaitMs 最大等待时间（毫秒）。超过此时间未检测到完成，强制退出。
     *                  根据项目大小和机器速度调整，建议 15000 ~ 120000。
     */
    public static void start(long maxWaitMs) {
        Thread watchdog = new Thread(() -> run(maxWaitMs), "datagen-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        LOGGER.info("DataGenWatchdog 已启动，最大等待 {} ms", maxWaitMs);
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

            if (mainThreadFinished()) {
                LOGGER.info("检测到数据生成完成，{} ms 后退出", POST_COMPLETE_DELAY_MS);
                sleepQuietly(POST_COMPLETE_DELAY_MS);
                forceExit();
                return;
            }
        }

        LOGGER.warn("看门狗超时（{} ms），强制退出", maxWaitMs);
        forceExit();
    }

    /** 检查 main 线程是否已经终止（数据生成完成） */
    private static boolean mainThreadFinished() {
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if ("main".equals(t.getName())
                    && t.getState() == Thread.State.TERMINATED) {
                return true;
            }
        }
        return false;
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private static void forceExit() {
        Runtime.getRuntime().halt(0);
    }
}