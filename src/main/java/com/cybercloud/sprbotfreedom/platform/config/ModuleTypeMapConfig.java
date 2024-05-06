package com.cybercloud.sprbotfreedom.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author liuyutang
 * @date 2023/9/6
 */
@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "system")
public class ModuleTypeMapConfig {

    private Map<String, List<String>> moduleTypeMap;

}
