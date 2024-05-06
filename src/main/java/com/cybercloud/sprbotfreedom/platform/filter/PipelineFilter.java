package com.cybercloud.sprbotfreedom.platform.filter;

import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

/**
 * 过滤器流水线
 * @author liuyutang
 * @date 2023/7/7
 */
@Slf4j
@Configurable
public class PipelineFilter implements Filter {

    @Autowired
    private FilterHandlerChain filterHandlerChain;
    @Autowired
    @Qualifier("bodyWapperHandler")
    private FilterHandler bodyWapperHandler;
    @Autowired
    @Qualifier("xssHandler")
    private FilterHandler xssHandler;
    @Autowired
    @Qualifier("authHandler")
    private FilterHandler authHandler;
    @Autowired
    @Qualifier("hostHandler")
    private FilterHandler hostHandler;
    @Autowired
    @Qualifier("idempotentTokenHandler")
    private FilterHandler idempotentTokenHandler;
    @Autowired
    @Qualifier("resourcePermHandler")
    private FilterHandler resourcePermHandler;
    @Autowired
    @Qualifier("interfacePermHandler")
    private FilterHandler interfacePermHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterHandlerChain
                .addHandler(bodyWapperHandler)
                .addHandler(xssHandler)
                .addHandler(hostHandler)
                .addHandler(authHandler)
                .addHandler(resourcePermHandler)
                .addHandler(interfacePermHandler)
                .addHandler(idempotentTokenHandler)
        ;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterHandlerChain.initChain(servletRequest, servletResponse, filterChain);
        filterHandlerChain.execute();
        log.info(">>> PipelineFilter doFilter done");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
