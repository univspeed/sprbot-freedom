package com.cybercloud.sprbotfreedom.platform.file;

import com.cybercloud.sprbotfreedom.platform.enums.FileRelatedEnum;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文件服务接口
 * @author liuyutang
 */
public interface FileService {

    /**
     * 上传单个文件
     * 文件保存逻辑：如果relatedId为空，则直接保存，否则先删除relatedId关联的所有文件，然后在保存到t_file_accessory
     * @param file 文件流
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @param resolveVidioTime 是否解析视频时间
     * @return 上传后的文件信息
     * @throws Exception
     */
    FileAccessoryEntity upload(MultipartFile file, String relatedType, String relatedTable, String relatedField, Long relatedId, boolean resolveVidioTime) throws Exception;

    /**
     * 上传多个文件
     * 文件保存逻辑：如果relatedId为空，则直接保存，否则先删除relatedId关联的所有文件，然后在保存到t_file_accessory
     * @param files 文件流数组
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @param resolveVidioTime 是否解析视频时间
     * @return 上传后的文件信息列表
     * @throws Exception
     */
    List<FileAccessoryEntity> upload(MultipartFile[] files, String relatedType, String relatedTable, String relatedField, Long relatedId, boolean resolveVidioTime) throws Exception;

    /**
     * 根据文件路径数组查找文件列表
     * @param filePaths
     * @return
     */
    List<FileAccessoryEntity> findByPath(String... filePaths);

    /**
     * 根据关系相关参数查询文件列表
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @return
     */
    List<FileAccessoryEntity> findByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId);

    /**
     * 根据文件id数组查询文件集合
     * @param relatedIdNull 是否过滤关系id为空的数据
     * @param fileIds 文件id数组
     * @return
     */
    List<FileAccessoryEntity> findByFileIds(boolean relatedIdNull, Long... fileIds);

    /**
     * 批量删除文件（逻辑删除）
     * @param fileIds 文件id数组
     * @return
     */
    Set<Long> logicDelFileByIds(Long... fileIds);

    /**
     * 批量删除文件（逻辑删除）
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @return
     */
    Set<Long> logicDelFileByRelatedParam(String relatedType,String relatedTable,String relatedField,Long relatedId);

    /**
     * 批量删除文件（物理删除）
     * @param fileIds 文件id数组
     * @return
     */
    Set<Long> delFileByIds(Long... fileIds);

    /**
     * 批量删除文件（物理删除）
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @return
     */
    Set<Long> delFileByRelatedParam(String relatedType,String relatedTable,String relatedField,Long relatedId);

    /**
     * 绑定文件与数据关系
     * @param fileIds 文件id集合
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedId 关联数据id
     * @return
     */
    Set<Long> bindFileAndDataId(Set<Long> fileIds,String relatedType,String relatedTable,Long relatedId);

    /**
     * 根据附件ID复制附件信息，并修改附件属性
     * @param fileIds 附件ID列表
     * @param fileRelatedEnum 附件上传属性
     * @param relatedId  业务数据id
     * @return
     */
    Set<Long> bindFileAndDataId(Set<Long> fileIds, FileRelatedEnum fileRelatedEnum, Long relatedId);

    /**
     * 根据关系ID复制附件信息
     * @param copyRelatedId 关联数据id
     * @param targetRelatedId 目标数据id
     * @return
     */
    List<FileAccessoryEntity> copyByRelatedId(Long copyRelatedId, Long targetRelatedId);

    /**
     * 根据关系ID复制附件信息，并修改附件属性
     * @param copyRelatedId 关联数据id
     * @param targetRelatedId 目标数据id
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @return
     */
    List<FileAccessoryEntity> copyByRelatedIdAndModifyRelated(Long copyRelatedId, Long targetRelatedId, String relatedType, String relatedTable, String relatedField);

    /**
     * 根据附件ID复制附件信息
     * @param fileIds 附件ID列表
     * @param targetRelatedId 目标数据id
     * @return
     */
    List<FileAccessoryEntity> copyByFileIds(Set<Long> fileIds, Long targetRelatedId);

    /**
     * 根据附件ID复制附件信息，并修改附件属性
     * @param fileIds 附件ID列表
     * @param targetRelatedId 目标数据id
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @return
     */
    List<FileAccessoryEntity> copyByFileIdsAndModifyRelated(Set<Long> fileIds, Long targetRelatedId, String relatedType, String relatedTable, String relatedField);

    /**
     * 根据附件ID获取字节数组
     * @param fileIds 保存成功的附件ID数组
     * @return 文件的字节数组
     */
    Map<Long,byte[]> findByteArrayByIds(Long... fileIds);

}
