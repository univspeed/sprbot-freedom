package com.cybercloud.sprbotfreedom.web.entity.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


/**
 * 用于接口业务id接收
 * @author liuyutang
 * @date 2023/7/11
 */
@Data
public class IdBO implements Serializable {
    /**
     * 业务数据ID
     */
    @NotNull(message = "id不能为空")
    private Long id;
}
