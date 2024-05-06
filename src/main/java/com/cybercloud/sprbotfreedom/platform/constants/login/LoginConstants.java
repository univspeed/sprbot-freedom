package com.cybercloud.sprbotfreedom.platform.constants.login;


import com.cybercloud.sprbotfreedom.platform.util.encrypt.SM3Util;

/**
 * 登录信息常量
 * @author liuyutang
 * @date 2023/7/7
 */
public class LoginConstants {
    /**
     * 缓存token的redis key 模板
     */
    public static final String TOKEN_CACHE_KEY_TEMPLATE = "%s:TOKEN";

    public static final String CREATE_SM3_PASSWORD_TEMPLATE = "%s-%s-%s";
    /**
     * 缓存token的redis key
     * @param username
     * @return
     */
    public static String getTokenCacheKey(String username){
        return String.format(TOKEN_CACHE_KEY_TEMPLATE,username);
    }

    /**
     * 创建SM3加密密码
     * @param username
     * @param salt
     * @param password
     * @return
     */
    public static String createSm3Pass(String username, String salt, String password){
        return SM3Util.createSM3(String.format(CREATE_SM3_PASSWORD_TEMPLATE, username, salt, password));
    }


}
