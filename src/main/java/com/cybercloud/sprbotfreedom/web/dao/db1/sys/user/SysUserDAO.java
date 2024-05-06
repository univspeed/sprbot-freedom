package com.cybercloud.sprbotfreedom.web.dao.db1.sys.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysUserEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 操作员数据接口
 * @author liuyutang
 * @date 2023/7/11
 */
@Mapper
@Repository
@DB1DataSource
public interface SysUserDAO extends BaseMapper<SysUserEntity> {
}
