package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.constants.login.LoginConstants;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import com.cybercloud.sprbotfreedom.platform.util.CacheUtil;
import com.cybercloud.sprbotfreedom.platform.util.JwtUtil;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证处理器
 * @author liuyutang
 * @date 2023/7/7
 */
@Slf4j
@Component("authHandler")
public class AuthFilterHandler implements FilterHandler {

    @Autowired
    private FilterHandlerChain filterHandlerChain;
    @Autowired
    private AuthService authService;
    @Value("${system.token.expire-time}")
    private Long tokenExpireTime;

    @Value("${system.auth-check.open-auth-check:true}")
    private boolean openAuthFilter;
    @Value("${system.auth-check.auth-white-list:}")
    private String whileAuthUrls;
    @Value("${system.auth-check.auth-deny-token-list:}")
    private String denyTokenList;
    @Value("${system.token.header:authorization}")
    private String headerKey;

    @Override
    public void handle() throws IOException {
        if(openAuthFilter){
            log.info(">>>>>>>>> 认证过滤器就绪");
            HttpServletRequest request = (HttpServletRequest) filterHandlerChain.getRequest();
            HttpServletResponse response = (HttpServletResponse) filterHandlerChain.getResponse();
            extracted(response);
            filterHandlerChain.setResponse(response);
            String requestURI = request.getRequestURI();
            if(white(whileAuthUrls,requestURI)){
                log.info("AuthFilterHandler - Request URL is in white list :{} skip auth",requestURI);
                return;
            }
            String token = request.getHeader(headerKey);
            if(denyTokenList.contains(token)){
                log.info("AuthFilterHandler - token is in deny list :{} refuse auth",token);
                ServiceException.throwError(SystemErrorCode.ERROR_401);
            }
            if(StringUtils.isBlank(token)){
                log.info("AuthFilterHandler - token is blank");
                ServiceException.throwError(SystemErrorCode.ERROR_410);
            }
            // 这一步会验证token，通过则获取用户信息，否则抛出异常被全局异常捕获返回结果
            UserInfo userInfo = JwtUtil.getUserInfo(token);
            if(userInfo == null){
                log.info("AuthFilterHandler - userInfo is null");
                ServiceException.throwError(SystemErrorCode.ERROR_410);
            }
            String cacheToken = (String)CacheUtil.get(LoginConstants.getTokenCacheKey(userInfo.getUsername()));
            if(StringUtils.isBlank(cacheToken)){
                log.info("AuthFilterHandler - cacheToken is blank");
                ServiceException.throwError(SystemErrorCode.ERROR_410);
            }
            // 如果一直在使用的话就更新一下时间
            CacheUtil.set(String.format(LoginConstants.getTokenCacheKey(userInfo.getUsername())),token,tokenExpireTime);
        }
    }

    /**
     * 设置响应头
     * @param response
     */
    private static void extracted(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");
    }
}
