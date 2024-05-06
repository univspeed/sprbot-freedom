package com.cybercloud.sprbotfreedom.platform.config;

import com.cybercloud.sprbotfreedom.platform.util.genid.IdWorker;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author liuyutang
 * @date 2023/7/10
 */
@Configurable
public class IdWorkerConfig {
    /**
     * 工作id
     */
    @Value("${system.idgen.work-id:1}")
    private Long workerId;
    /**
     * 数据中心id
     */
    @Value("${system.idgen.datacenter-id:1}")
    private Long datacenterId;
    /**
     * 12位的序列号
     */
    @Value("${system.idgen.sequence:1}")
    private Long sequence;

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(workerId, datacenterId, sequence);
    }
}
