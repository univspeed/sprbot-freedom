package com.cybercloud.sprbotfreedom.platform.util;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动态表单-HttpServletRequest转Map工具类
 * @author liuyutang
 */
public class RequestUtil {
    /**
     * 请求转Map
     * @param request 请求
     * @return Map<String,Object>
     */
    public static Map<String,Object> initParameters(HttpServletRequest request){
        Map<String,Object> parameters=new HashMap<>(16);
        parameters.put("parameters", new LinkedHashMap<String,String[]>(request.getParameterMap()));
        parameters.put("_url",request.getRequestURI());
        parameters.put("_cookie",request.getCookies());
        parameters.put("_ip",request.getRemoteAddr());
        //甯﹀ご閮ㄤ俊鎭?
        JSONObject header=new JSONObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            header.put(name, request.getHeader(name));
        }
        parameters.put("_header",header);
        String rootPath=request.getSession().getServletContext().getRealPath("/");
        parameters.put("_rootPath",rootPath);

        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        parameters.put("_basePath",basePath);

        return parameters;
    }
}
