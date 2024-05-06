package com.cybercloud.sprbotfreedom.platform.filter.chain;

import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;


/**
 * 处理器上下文接口
 * @author liuyutang
 * @date 2023/7/7
 */
public interface FilterHandlerChain {
    /**
     * 初始化责任链参数
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     */
    void initChain(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain);

    /**
     * 添加处理器
     * @param handler
     * @return
     */
    FilterHandlerChain addHandler(FilterHandler handler);

    /**
     * 执行处理器
     * @throws ServletException
     * @throws IOException
     */
    void execute() throws ServletException, IOException;

    /**
     * 获取请求参数
     * @return
     */
    ServletRequest getRequest();

    /**
     * 获取响应参数
     * @return
     */
    ServletResponse getResponse();

    /**
     * 获取过滤器链
     * @return
     */
    FilterChain getChain();

    /**
     * 设置请求参数
     * @param servletRequest
     */
    void setRequest(ServletRequest servletRequest);

    /**
     * 设置响应参数
     * @param servletResponse
     */
    void setResponse(ServletResponse servletResponse);
}
