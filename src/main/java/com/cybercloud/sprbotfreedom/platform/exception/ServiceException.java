package com.cybercloud.sprbotfreedom.platform.exception;

import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import lombok.Data;

/**
 * 公共业务异常
 * @author liuyutang
 * @date 2023/7/6
 */
@Data
public class ServiceException extends RuntimeException{
    /**
     * 错误代码
     */
    private String code;
    /**
     * 错误信息
     */
    private String message;

    public ServiceException(SystemErrorCode systemErrorCode) {
        this.code = systemErrorCode.getCode();
        this.message = systemErrorCode.getMsg();
    }

    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 创建业务异常类
     * @param systemErrorCode
     * @return
     */
    public static ServiceException create(SystemErrorCode systemErrorCode){
        return new ServiceException(systemErrorCode);
    }

    /**
     * 抛出异常
     * @param systemErrorCode
     */
    public static void throwError(SystemErrorCode systemErrorCode){
        throw create(systemErrorCode);
    }

    /**
     * 抛出异常
     * @param code
     * @param message
     */
    public static void throwError(String code,String message){
        throw new ServiceException(code,message);
    }
}
