package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import com.cybercloud.sprbotfreedom.platform.wrapper.XssHttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Xss处理器
 * @author liuyutang
 */
@Slf4j
@Component("xssHandler")
public class XssFilterHandler implements FilterHandler {

    @Value("${system.xss-check.open-xss-check:false}")
    public boolean openXssProtect;

    @Value("${system.xss-check.xss-white-list:}")
    private String whiteListUrls;

    private final FilterHandlerChain filterHandlerChain;

    public XssFilterHandler(FilterHandlerChain filterHandlerChain) {
        this.filterHandlerChain = filterHandlerChain;
    }

    @Override
    public void handle(){
        if(openXssProtect){
            log.info(">>>>>>>>> 防XSS注入过滤器就绪");
            HttpServletRequest request = (HttpServletRequest) filterHandlerChain.getRequest();
            String requestUri = request.getRequestURI();
            if(white(whiteListUrls,requestUri)){
                log.info("XssFilterHandler - URI is in white list :{} skip xss check",requestUri);
            }else{
                filterHandlerChain.setRequest(new XssHttpServletRequestWrapper(request));
            }
        }
    }

}
