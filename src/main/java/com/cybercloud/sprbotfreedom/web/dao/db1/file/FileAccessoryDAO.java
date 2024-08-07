package com.cybercloud.sprbotfreedom.web.dao.db1.file;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 附件数据层接口
 * @author liuyutang
 * @date 2023/7/7
 */
@Mapper
@Repository
@DB1DataSource
@CacheNamespace
public interface FileAccessoryDAO extends BaseMapper<FileAccessoryEntity> {
}
