package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuyutang
 * @date 2023/7/10
 */
@Slf4j
@Component("hostHandler")
public class HostFilterHandler implements FilterHandler {

    private final FilterHandlerChain filterHandlerChain;
    @Value("${system.host-check.open-host-check:false}")
    private boolean openHostFilter;
    @Value("${system.host-check.host-white-list:}")
    private String whiteListUrlsString;

    public HostFilterHandler(FilterHandlerChain filterHandlerChain) {
        this.filterHandlerChain = filterHandlerChain;
    }

    @Override
    public void handle() throws IOException {
        if(openHostFilter){
            log.info(">>>>>>>>> Host地址过滤器就绪");
            HttpServletRequest request = (HttpServletRequest) filterHandlerChain.getRequest();
            String host = request.getRemoteAddr();
            if(StringUtils.isNotBlank(whiteListUrlsString)) {
                List<String> whiteListUrls = Arrays.asList(whiteListUrlsString.split(","));
                if(CollectionUtils.isNotEmpty(whiteListUrls)){
                    if(whiteListUrls.contains(host)){
                        log.info("HostHandler - Host is in white list :{} skip auth",host);
                    }else{
                        ServiceException.throwError(SystemErrorCode.ERROR_403);
                    }
                }
            }
        }
    }
}
