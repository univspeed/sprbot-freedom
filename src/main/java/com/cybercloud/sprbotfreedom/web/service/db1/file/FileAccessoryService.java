package com.cybercloud.sprbotfreedom.web.service.db1.file;

import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;

import java.util.List;
import java.util.Set;

/**
 * 附件业务接口
 * @author liuyutang
 * @date 2023/7/7
 */
public interface FileAccessoryService extends BaseService<FileAccessoryEntity> {
    /**
     * 保存附件信息
     * @param fils
     * @return
     */
    List<FileAccessoryEntity> saveAccessoryFiles(List<FileAccessoryEntity> fils);

    /**
     * 根据参数查询附件列表
     * @param relatedType
     * @param relatedTable
     * @param relatedField
     * @param relatedId
     * @param filePaths
     * @param fileIds
     * @param relatedIdNull
     * @return
     */
    List<FileAccessoryEntity> findByParams(String relatedType, String relatedTable, String relatedField, Long relatedId, String[] filePaths, Long[] fileIds, boolean relatedIdNull);

    /**
     * 根据参数删除附件
     * @param relatedType
     * @param relatedTable
     * @param relatedField
     * @param relatedId
     * @param delIds
     * @param b
     * @return
     */
    Set<Long> delFileByParams(String relatedType, String relatedTable, String relatedField, Long relatedId, Long[] delIds, boolean b);

    /**
     * 绑定附件信息
     * @param fileIds
     * @param relatedType
     * @param relatedTable
     * @param relatedId
     * @return
     */
    Set<Long> bindFileAndDataId(Set<Long> fileIds, String relatedType, String relatedTable, Long relatedId);

    /**
     * 单日上传数量限制开关
     * @param key
     * @return
     */
    <T> T loadBykey(String key,Class<T> clazz);
}
