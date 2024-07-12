package com.cybercloud.sprbotfreedom.web.dao.db1.sys.job;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.job.SysJobEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 定时任务管理数据层接口
 * @author liuyutang
 * @date 2023/8/2
 */
@Mapper
@Repository
@DB1DataSource
@CacheNamespace
public interface SysJobDAO extends BaseMapper<SysJobEntity> {

}
