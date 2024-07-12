package com.cybercloud.sprbotfreedom.web.entity.bo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuyutang
 * @date 2023/7/11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPassBO implements Serializable {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 旧密码
     */
    private String oldPass;
    /**
     * 新密码
     */
    private String newPass;
    /**
     * 确认密码
     */
    private String comfirmPass;
}
