package com.cybercloud.sprbotfreedom.web.entity.bo.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录信息业务数据载体
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
public class LoginBO  implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码Key
     */
    private String key;
    /**
     * 验证码
     */
    private String captcha;
    /**
     * 记住我
     */
    private boolean remenberMe;
}
