package com.cybercloud.sprbotfreedom.platform.base.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 幂等token载体
 * @author liuyutang
 * @date 2023/7/10
 */
@Data
@Builder
public class IdempotentTokenEntity {
    private String key;
    private String token;
}
