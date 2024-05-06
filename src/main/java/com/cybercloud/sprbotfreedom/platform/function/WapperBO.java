package com.cybercloud.sprbotfreedom.platform.function;

/**
 * 业务实体包装函数式接口
 * @author liuyutang
 * @param <T>
 */
@FunctionalInterface
public interface WapperBO<T> {
    /**
     * 包装业务实体信息
     * @param e
     * @param args
     * @return <E>
     */
    void wapper(T e,Object... args);
}