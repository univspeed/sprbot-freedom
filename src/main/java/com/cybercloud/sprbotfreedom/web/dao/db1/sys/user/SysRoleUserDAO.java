package com.cybercloud.sprbotfreedom.web.dao.db1.sys.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysRoleUserEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuyutang
 * @date 2024/2/29
 */
@Mapper
@Repository
@DB1DataSource
public interface SysRoleUserDAO extends BaseMapper<SysRoleUserEntity> {
}
