package com.cybercloud.sprbotfreedom.platform.base.entity;

import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author liuyutang
 * @date 2023/7/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * 响应代码
     */
    private String code;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 响应状态
     */
    private boolean success;
    /**
     * 响应数据体
     */
    private T data;

    /**
     * 默认的响应成功方法
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result success(T data){
        return Result.<T>builder()
                .code(SystemErrorCode.ERROR_200.getCode())
                .message(SystemErrorCode.ERROR_200.getMsg())
                .success(true)
                .data(data)
                .build();
    }

    /**
     * 默认的响应失败方法
     * @return
     * @param <T>
     */
    public static <T> Result fail(){
        return Result.<T>builder()
                .code(SystemErrorCode.ERROR_999.getCode())
                .message(SystemErrorCode.ERROR_999.getMsg())
                .success(false)
                .build();
    }

    /**
     * 默认的响应失败方法
     * @return
     * @param <T>
     */
    public static <T> Result fail(SystemErrorCode systemErrorCode){
        return Result.<T>builder()
                .code(systemErrorCode.getCode())
                .message(systemErrorCode.getMsg())
                .success(false)
                .build();
    }

    /**
     * 默认的响应失败方法
     * @param code
     * @param message
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result fail(String code,String message, T data){
        return Result.<T>builder()
                .code(code)
                .message(message)
                .success(false)
                .data(data)
                .build();
    }
}
