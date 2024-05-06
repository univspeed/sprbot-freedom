package com.cybercloud.sprbotfreedom.web.entity.bo.user;

import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysUserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * 保存用户业务实体
 * @author liuyutang
 * @date 2023/7/12
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserBO extends SysUserEntity {
    /**
     * 保存密码
     */
    private String savePass;
    private Set<Long> roles;
}
