package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Http请求Body包装处理器
 * @author liuyutang
 */
@Component("bodyWapperHandler")
public class BodyWapperFillterHandler implements FilterHandler {

    private final FilterHandlerChain filterHandlerChain;

    public BodyWapperFillterHandler(FilterHandlerChain filterHandlerChain) {
        this.filterHandlerChain = filterHandlerChain;
    }

    @Override
    public void handle() throws IOException {
        /*ServletRequest servletRequest = filterHandlerChain.getRequest();
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        }
        if (null != requestWrapper) {
            filterHandlerChain.setRequest(requestWrapper);
        }*/
    }
}
