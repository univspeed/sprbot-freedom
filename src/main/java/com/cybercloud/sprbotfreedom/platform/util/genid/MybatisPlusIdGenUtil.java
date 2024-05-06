package com.cybercloud.sprbotfreedom.platform.util.genid;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 自定义id生成器，全局雪花算法
 * @author liuyutang
 * @date 2023/7/11
 */
@Primary
@Component
public class MybatisPlusIdGenUtil implements IdentifierGenerator {

    @Autowired
    private IdWorker idWorker;

    @Override
    public Long nextId(Object entity) {
        return idWorker.nextId();
    }
}
