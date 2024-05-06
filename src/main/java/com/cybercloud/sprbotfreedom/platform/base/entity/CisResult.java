package com.cybercloud.sprbotfreedom.platform.base.entity;

import lombok.Data;

import java.util.List;

/**
 * @author liuyutang
 * @date 2023/9/19
 */
@Data
public class CisResult<T> {
    private int returnCode;
    private String description;
    private List<T> data;
}
