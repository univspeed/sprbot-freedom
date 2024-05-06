package com.cybercloud.sprbotfreedom.platform.filter.handler;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 处理器接口
 * @author liuyutang
 * @date 2023/7/7
 */
public interface FilterHandler {
    void handle() throws IOException;

    /**
     * 是否是白名单
     * @param url    路由
     * @return true/false
     */
    default boolean white(String whileAuthUrls, String url) {
        if(StringUtils.isNotBlank(whileAuthUrls)){
            for (String whiteUrl : whileAuthUrls.split(",")) {
                // 对特殊字符进行转义处理
                whiteUrl = whiteUrl.replaceAll("\\*", ".*");
                if(Pattern.matches(whiteUrl, url)){
                    return true;
                }
            }
        }
        return false;
    }
}
