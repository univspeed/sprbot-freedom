package com.cybercloud.sprbotfreedom.web.dao.db1.sys.resource;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuyutang
 * @date 2023/7/12
 */
@Mapper
@Repository
@DB1DataSource
public interface ResourceDAO extends BaseMapper<SysResourceEntity> {
}
