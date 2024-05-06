package com.cybercloud.sprbotfreedom.platform.util.genid;

import java.util.UUID;

/**
 * UUID生成工具
 * @author liuyutang
 * @date 2023/7/6
 */
public class UUIDUtil {
    /**
     * 生成UUID
     * @return
     */
    public static String genUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
