package com.cybercloud.sprbotfreedom.platform.base.service;

import com.cybercloud.sprbotfreedom.platform.base.entity.IdempotentTokenEntity;

/**
 * 接口幂等性token服务接口
 * @author liuyutang
 */
public interface IdempotentTokenService {

    /**
     * 获取token
     * @return
     */
    IdempotentTokenEntity getToken();

    /**
     * 校验token
     * @param token
     * @return
     */
    boolean checkToken(String token);
}
