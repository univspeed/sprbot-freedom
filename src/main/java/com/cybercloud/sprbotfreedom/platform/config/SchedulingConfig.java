package com.cybercloud.sprbotfreedom.platform.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务配置
 * @author liuyutang
 * @date 2023/8/2
 */
@Configurable
public class SchedulingConfig {
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(4);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("JClouldManager-TaskSchedulerThreadPool-");
        return taskScheduler;
    }
}
