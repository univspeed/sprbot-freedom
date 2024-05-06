package com.cybercloud.sprbotfreedom.platform.util;


import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 用户信息工具类
 * @author liuyutang
 */
@Slf4j
@Component
public class GetUserInfoUtil {

    @Value("${system.token.header:authorization}")
    private String headerKey;
    @Value("${system.auth-check.open-auth-check}")
    private boolean openAuthCheck;
    private static GetUserInfoUtil getUserInfoUtil;

    private GetUserInfoUtil() {
        getUserInfoUtil = this;
        getUserInfoUtil.headerKey = this.headerKey;
        getUserInfoUtil.openAuthCheck = this.openAuthCheck;
    }


    /**
     * 获取当前登录用户的信息
     * @param request
     * @return
     */
    public static UserInfo getUserInfo(HttpServletRequest request) {
        UserInfo userInfo;
        if(getUserInfoUtil == null || !getUserInfoUtil.openAuthCheck){
            return null;
        }
        try {
            userInfo = JwtUtil.getUserInfo(request.getHeader(getUserInfoUtil.headerKey));
            log.info("获取用户信息：{}", userInfo);
        } catch (Exception e) {
            userInfo = null;
            //throw new ServiceException(SystemErrorCode.ERROR_410);
        }
        return userInfo;
    }
}