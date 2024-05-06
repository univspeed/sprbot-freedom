package com.cybercloud.sprbotfreedom.web.dao.db1.sys.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleResourceEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 角色资源关联数据层接口
 * @author liuyutang
 * @date 2023/7/12
 */
@Mapper
@Repository
@DB1DataSource
public interface RoleResourceDAO extends BaseMapper<SysRoleResourceEntity> {
}
