package com.cybercloud.sprbotfreedom.platform.config;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liuyutang
 * @date 2023/7/7
 */
@RestControllerAdvice
public class LoginUserConfig {

    @Value("${system.token.header:authorization}")
    private String headerKey;
    @Value("${system.auth-check.open-auth-check}")
    private boolean openAuthCheck;
    @Value("${system.auth-check.auth-white-list:}")
    private String authWhiteListString;

    @ModelAttribute("userInfo")
    public UserInfo getLoginUserInfo(HttpServletRequest request) {
        if(openAuthCheck){
            String requestURI = request.getRequestURI();
            if(StringUtils.isNotBlank(authWhiteListString)) {
                List<String> whiteListUrls = Arrays.asList(authWhiteListString.split(","));
                if(CollectionUtils.isNotEmpty(whiteListUrls) && white(whiteListUrls,requestURI)){
                    return null;
                }
            }
            // 检测有没有传token，没有则返回空
            String token = request.getHeader(headerKey);
            if (StringUtils.isAllEmpty(token)) {
                return null;
            }
            return getUserInfoFromToken(token);
        }
       return null;
    }

    /**
     * 从token中获取用户信息
     * @param token
     * @return
     */
    public static UserInfo getUserInfoFromToken(String token) {
        if (JwtUtil.isExpiration(token)) {
            ServiceException.throwError(SystemErrorCode.ERROR_410);
        }
        return JwtUtil.getUserInfo(token);
    }

    /**
     * 校验白名单
     * @param whileAuthUrls
     * @param url
     * @return
     */
    public boolean white(List<String> whileAuthUrls, String url) {
        if(CollectionUtils.isNotEmpty(whileAuthUrls)){
            for (String whiteUrl : whileAuthUrls) {
                // 对特殊字符进行转义处理
                whiteUrl = whiteUrl.replaceAll("\\*", ".*");;
                if(Pattern.matches(whiteUrl, url)){
                    return true;
                }
            }
        }
        return false;
    }
}
