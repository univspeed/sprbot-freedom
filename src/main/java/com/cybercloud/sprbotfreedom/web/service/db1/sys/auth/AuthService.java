package com.cybercloud.sprbotfreedom.web.service.db1.sys.auth;

import com.cybercloud.sprbotfreedom.web.entity.bo.auth.AuthBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.auth.LoginBO;

/**
 * 认证服务
 * @author liuyutang
 * @date 2023/7/12
 */
public interface AuthService {
    /**
     * 登录接口
     * @param loginBO
     * @return
     */
    AuthBO login(LoginBO loginBO);

    /**
     * 登出接口
     * @return
     */
    boolean logout(String token);
}
