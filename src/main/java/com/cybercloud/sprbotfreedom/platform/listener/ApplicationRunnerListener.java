package com.cybercloud.sprbotfreedom.platform.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @author liuyutang
 * @date 2023/7/17
 */
@Slf4j
@Component
public class ApplicationRunnerListener implements ApplicationListener {


    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        // 在这里可以监听到Spring Boot的生命周期
        // 初始化环境变量
        if (applicationEvent instanceof ApplicationEnvironmentPreparedEvent) {
            log.debug("Application environment prepared");
        }
        // 初始化完成
        else if (applicationEvent instanceof ApplicationPreparedEvent) {
            log.debug("Application prepared");
        }
        // 应用刷新
        else if (applicationEvent instanceof ContextRefreshedEvent) {
            log.debug("Application refreshed");
            // 可以进行一些操作
        }
        // 应用已启动完成
        else if (applicationEvent instanceof ApplicationReadyEvent) {
            log.debug("Application ready");
        }
        // 应用启动，需要在代码动态添加监听器才可捕获
        else if (applicationEvent instanceof ContextStartedEvent) {
            log.debug("Application started");
        }
        // 应用停止
        else if (applicationEvent instanceof ContextStoppedEvent) {
            log.debug("Application stopped");
        }
        // 应用关闭
        else if (applicationEvent instanceof ContextClosedEvent) {
            log.debug("Application closed");
        }
        else {
            log.debug("Application event: {}", applicationEvent);
        }
    }
}
