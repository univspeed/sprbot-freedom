package com.cybercloud.sprbotfreedom.platform.config;

import com.alibaba.fastjson.JSONObject;
import com.cybercloud.sprbotfreedom.platform.base.entity.Result;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一返回值处理
 * @author liuyutang
 */
@Slf4j
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 防止二次封装
        if (o instanceof Result) {
            return o;
        }
        // 防止异常跳过捕获直接抛出
        else if (o instanceof LinkedHashMap){
            Map<String,Object> error = (Map)o;
            log.error("{}",error);
            SystemErrorCode message = SystemErrorCode.findByMsg((String) error.get("message"));
            return Result.fail(
                    message == null ? SystemErrorCode.ERROR_999.getCode() : message.getCode(),
                    message == null ? (String)error.get("message") : message.getMsg(),(String)error.get("path")
            );
        }
        // 单独处理String类型的返回值
        if (o instanceof String) {
            return JSONObject.toJSONString(Result.success(o));
        }
        return Result.success(o);
    }
}
