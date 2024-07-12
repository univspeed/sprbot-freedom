package com.cybercloud.sprbotfreedom.web.entity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于接口业务id接收
 * @author liuyutang
 * @date 2023/4/20
 */
@Data
public class IdsBO implements Serializable {
    /**
     * ids
     */
    private Long[] ids;
}
