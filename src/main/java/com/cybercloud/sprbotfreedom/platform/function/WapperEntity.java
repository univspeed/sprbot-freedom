package com.cybercloud.sprbotfreedom.platform.function;

import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;

/**
 * 实体包装函数式接口
 * @author liuyutang
 * @param <E>
 */
@FunctionalInterface
public interface WapperEntity<E extends BaseEntity> {
    /**
     * 包装实体信息
     * @param e
     * @return <E>
     */
    E wapper(E e);
}