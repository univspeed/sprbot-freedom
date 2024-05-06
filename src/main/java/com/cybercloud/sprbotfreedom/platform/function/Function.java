package com.cybercloud.sprbotfreedom.platform.function;

/**
 * 函数接口
 * @author liuyutang
 * @date 2023/10/16
 */
@FunctionalInterface
public interface Function<T> {
    /**
     * 执行某方法
     * @param params
     * @return
     */
    T invoke(Object... params);
}
