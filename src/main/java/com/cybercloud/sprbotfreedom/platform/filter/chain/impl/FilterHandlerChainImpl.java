package com.cybercloud.sprbotfreedom.platform.filter.chain.impl;

import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 处理器上下文实现
 * @author liuyutang
 * @date 2023/7/7
 */
@Slf4j
@Component
@Configurable
@NoArgsConstructor
public class FilterHandlerChainImpl implements FilterHandlerChain {

    private List<FilterHandler> handlers = new ArrayList<>();
    private ServletRequest servletRequest;
    private ServletResponse servletResponse;
    private FilterChain filterChain;
    private final Lock lock = new ReentrantLock();

    @Override
    public void initChain(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.filterChain = filterChain;
    }

    @Override
    public FilterHandlerChain addHandler(FilterHandler handler) {
            handlers.add(handler);
        return this;
    }

    @Override
    public void execute() throws ServletException, IOException {
        if(CollectionUtils.isNotEmpty(handlers)) {
            for (FilterHandler handler : handlers) {
                lock.lock();
                try {
                    handler.handle();
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    @Override
    public ServletRequest getRequest() {
        return this.servletRequest;
    }

    @Override
    public ServletResponse getResponse() {
        return this.servletResponse;
    }

    @Override
    public FilterChain getChain() {
        return this.filterChain;
    }

    @Override
    public void setRequest(ServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    @Override
    public void setResponse(ServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }
}
