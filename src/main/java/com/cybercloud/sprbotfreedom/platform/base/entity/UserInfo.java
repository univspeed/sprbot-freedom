package com.cybercloud.sprbotfreedom.platform.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuyutang
 * @date 2023/7/6
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String username;
    private Long roleId;
    private String roleName;
}
