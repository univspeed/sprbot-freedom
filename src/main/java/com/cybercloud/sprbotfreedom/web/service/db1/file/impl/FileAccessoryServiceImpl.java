package com.cybercloud.sprbotfreedom.web.service.db1.file.impl;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevel;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevelRetention;
import com.cybercloud.sprbotfreedom.web.dao.db1.file.FileAccessoryDAO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.file.FileAccessoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 附件业务接口实现
 * @author liuyutang
 * @date 2023/7/7
 */
@Service
@PrintFunctionLog(level = PrintLevel.DEBUG,retention = PrintLevelRetention.UP)
@DB1DataSource
public class FileAccessoryServiceImpl extends BaseServiceImpl<FileAccessoryEntity, FileAccessoryDAO> implements FileAccessoryService {

    @Override
    public List<FileAccessoryEntity> saveAccessoryFiles(List<FileAccessoryEntity> fils) {
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByParams(String relatedType, String relatedTable, String relatedField, Long relatedId, String[] filePaths, Long[] fileIds, boolean relatedIdNull) {
        return null;
    }

    @Override
    public Set<Long> delFileByParams(String relatedType, String relatedTable, String relatedField, Long relatedId, Long[] delIds, boolean b) {
        return null;
    }

    @Override
    public Set<Long> bindFileAndDataId(Set<Long> fileIds, String relatedType, String relatedTable, Long relatedId) {
        return null;
    }

    @Override
    public <T> T loadBykey(String key, Class<T> clazz) {
        return null;
    }
}
