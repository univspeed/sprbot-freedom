package com.cybercloud.sprbotfreedom.platform.config;

import io.undertow.UndertowOptions;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Undertow
 * WebServerFactory 配置
 */
@Component
public class CustomizationBean implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }

    /**
     * http2支持
     *
     * @return org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
     * @author ming
     * @date 2022/7/14 11:06
     * @see CustomizationBean
     */
    @Bean
    public UndertowServletWebServerFactory undertowServletWebServerFactory() {
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addBuilderCustomizers(
                builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                        .setServerOption(UndertowOptions.HTTP2_SETTINGS_ENABLE_PUSH, true)
        );

        return factory;
    }
}