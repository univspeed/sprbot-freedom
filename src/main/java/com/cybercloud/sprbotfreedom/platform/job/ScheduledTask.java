package com.cybercloud.sprbotfreedom.platform.job;

import java.util.concurrent.ScheduledFuture;

/**
 * @author liuyutang
 * @date 2023/8/2
 */
public class ScheduledTask {
    volatile ScheduledFuture<?> future;

    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
