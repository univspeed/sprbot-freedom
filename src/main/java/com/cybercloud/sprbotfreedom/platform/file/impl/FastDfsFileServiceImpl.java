package com.cybercloud.sprbotfreedom.platform.file.impl;

import com.cybercloud.sprbotfreedom.platform.enums.FileRelatedEnum;
import com.cybercloud.sprbotfreedom.platform.util.genid.IdWorker;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.file.FileAccessoryService;
import com.cybercloud.sprbotfreedom.platform.file.FileService;

import com.cybercloud.sprbotfreedom.platform.file.strategy.FileServiceStrategy;
import com.cybercloud.sprbotfreedom.platform.util.file.FastDFSClientWrapper;
import com.cybercloud.sprbotfreedom.platform.util.file.FileCheckUtil;
import com.cybercloud.sprbotfreedom.platform.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分布式文件系统文件服务实现
 * @author liuyutang
 */
@Primary
@Service
@Slf4j
public class FastDfsFileServiceImpl implements FileService, InitializingBean {

    @Autowired
    private FileServiceStrategy fileServiceStrategy;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;
    @Autowired
    private FileAccessoryService fileAccessoryService;
    @Autowired
    private IdWorker idWorker;

    @Override
    public void afterPropertiesSet() throws Exception {
        fileServiceStrategy.register(FileServiceStrategy.FAST_DFS,this);
        log.info(">>> Add Fast DSF upload strategy service");
    }

    @Override
    public FileAccessoryEntity upload(MultipartFile file, String relatedType, String relatedTable, String relatedField, Long relatedId, boolean resolveVidioTime) throws Exception {
        log.info("上传单个文件接口接收参数:[file:{},relatedType:{},relatedTable:{},relatedField:{},relatedId:{},resolveVidioTime:{}]",file,relatedType,relatedTable,relatedField,relatedId,resolveVidioTime);
            //本地上传
            List<FileAccessoryEntity> fileAccessoryPOS = transformFiles(new MultipartFile[]{file}, relatedType, relatedTable, relatedField, relatedId,resolveVidioTime);
            if(CollectionUtils.isNotEmpty(fileAccessoryPOS)){
                List<FileAccessoryEntity> result = fileAccessoryService.saveAccessoryFiles(fileAccessoryPOS);
                if(CollectionUtils.isNotEmpty(result)){
                     return result.get(0);
                }
            }
//        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> upload(MultipartFile[] files, String relatedType, String relatedTable, String relatedField, Long relatedId, boolean resolveVidioTime) throws Exception {
        log.info("上传多个文件接口接收参数:[files:{},relatedType:{},relatedTable:{},relatedField:{},relatedId:{},resolveVidioTime:{}]",files,relatedType,relatedTable,relatedField,relatedId,resolveVidioTime);
        //如果关系id不为空则先执行删除
        if(null != relatedId){
            logicDelFileByRelatedParam(relatedType,relatedTable,relatedField,relatedId);
        }
        //本地上传
        List<FileAccessoryEntity> fileAccessoryEntities = transformFiles(files, relatedType, relatedTable, relatedField, relatedId,resolveVidioTime);
        if(CollectionUtils.isNotEmpty(fileAccessoryEntities)){
            return fileAccessoryService.saveAccessoryFiles(fileAccessoryEntities);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByPath(String... filePaths) {
        log.info("根据文件路径数组查找文件列表接口接收参数:[filePaths:{}]",filePaths);
        if(ArrayUtils.isNotEmpty(filePaths)){
            return fileAccessoryService.findByParams(null,null,null,null,filePaths,null,false);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId) {
        log.info("根据关系相关参数查询文件列表接口接收参数:[relatedType:{},relatedTable:{},relatedField:{},relatedId:{}]",relatedType,relatedTable,relatedField,relatedId);
        if(relatedId != null){
            return fileAccessoryService.findByParams(relatedType,relatedTable,relatedField,relatedId,null,null,false);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByFileIds(boolean relatedIdNull,Long... fileIds) {
        log.info("根据文件id数组查询文件集合接口接收参数:[relatedIdNull:{},fileIds:{}]", relatedIdNull,fileIds);
        if(ArrayUtils.isNotEmpty(fileIds)){
            return fileAccessoryService.findByParams(null,null,null,null,null,fileIds, relatedIdNull);
        }
        return null;
    }

    @Override
    public Set<Long> logicDelFileByIds(Long... fileIds) {
        log.info("批量删除文件（逻辑删除）接口接收参数:[fileIds:{}]", fileIds);
        if(ArrayUtils.isNotEmpty(fileIds)){
            return fileAccessoryService.delFileByParams(null,null,null,null,fileIds,true);
        }
        return null;
    }

    @Override
    public Set<Long> logicDelFileByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId) {
        log.info("批量删除文件（逻辑删除）接口接收参数:[relatedType:{},relatedTable:{},relatedField:{},relatedId:{}]",relatedType,relatedTable,relatedField,relatedId);
        if(StringUtils.isNoneBlank(relatedType,relatedTable,relatedField) && relatedId != null){
            return fileAccessoryService.delFileByParams(relatedType,relatedTable,relatedField,relatedId,null,true);
        }
        return null;
    }

    @Override
    public Set<Long> delFileByIds(Long... fileIds) {
        log.info("批量删除文件（物理删除）接口接收参数:[fileIds:{}]", fileIds);
        if(ArrayUtils.isNotEmpty(fileIds)){
            List<FileAccessoryEntity> byFileIds = findByFileIds(false,fileIds);
            if(CollectionUtils.isNotEmpty(byFileIds)){
                Set<Long> delIds = delFastDfs(byFileIds);
                if(CollectionUtils.isNotEmpty(delIds)){
                    return fileAccessoryService.delFileByParams(null, null, null, null,delIds.toArray(new Long[delIds.size()]), false);
                }
            }
        }
        return null;
    }


    @Override
    public Set<Long> delFileByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId) {
        log.info("批量删除文件（物理删除）接口接收参数:[relatedType:{},relatedTable:{},relatedField:{},relatedId:{}]",relatedType,relatedTable,relatedField,relatedId);
        if(StringUtils.isNoneBlank(relatedType,relatedTable,relatedField) && relatedId != null){
            List<FileAccessoryEntity> byFileIds = findByRelatedParam(relatedType,relatedTable,relatedField,relatedId);
            if(CollectionUtils.isNotEmpty(byFileIds)){
                Set<Long> delIds = delFastDfs(byFileIds);
                if(CollectionUtils.isNotEmpty(delIds)){
                    return fileAccessoryService.delFileByParams(null, null, null, null,delIds.toArray(new Long[delIds.size()]), false);
                }
            }
        }
        return null;
    }

    @Override
    public Set<Long> bindFileAndDataId(Set<Long> fileIds, String relatedType, String relatedTable, Long relatedId) {
        log.info("绑定文件与数据关系接口接收参数:[fileIds:{},relatedType:{},relatedTable:{},relatedId:{}]",fileIds,relatedType,relatedTable,relatedId);
        if(StringUtils.isNoneBlank(relatedType,relatedTable) && null != relatedId){
            //查询原来所有的附件
            List<FileAccessoryEntity> byRelatedParam = findByRelatedParam(relatedType, relatedTable, null, relatedId);
            if(CollectionUtils.isNotEmpty(byRelatedParam)){
                //查询需要被删除的附件
                Set<Long> delIds ;
                //为空删除所有
                if(CollectionUtils.isEmpty(fileIds)){
                    delIds = byRelatedParam.stream().map(FileAccessoryEntity::getId).collect(Collectors.toSet());
                }
                //否则删除不存在的
                else {
                    delIds = byRelatedParam.stream().filter(f -> !fileIds.contains(f.getId())).map(FileAccessoryEntity::getId).collect(Collectors.toSet());
                }
                if(CollectionUtils.isNotEmpty(delIds)){
                    logicDelFileByIds(delIds.toArray(new Long[delIds.size()]));
                }
            }
            return fileAccessoryService.bindFileAndDataId(fileIds,relatedType,relatedTable,relatedId);
        }
        return null;
    }

    @Override
    public Set<Long> bindFileAndDataId(Set<Long> fileIds, FileRelatedEnum fileRelatedEnum, Long relatedId) {
        return bindFileAndDataId(fileIds, fileRelatedEnum.getRelatedType(), fileRelatedEnum.getRelatedTable(), relatedId);
    }

    @Override
    public List<FileAccessoryEntity> copyByRelatedId(Long copyRelatedId, Long targetRelatedId) {
        log.info("根据关系ID复制附件信息接口接收参数:[copyRelatedId:{},targetRelatedId:{}]",copyRelatedId,targetRelatedId);
        if(null != copyRelatedId && null != targetRelatedId){
            List<FileAccessoryEntity> byRelatedParam = findByRelatedParam(null, null, null, copyRelatedId);
            if(CollectionUtils.isNotEmpty(byRelatedParam)) {
                byRelatedParam.forEach(fileAccessoryEntity -> {
                    fileAccessoryEntity.setId(idWorker.nextId());
                    fileAccessoryEntity.setRelatedId(targetRelatedId);
                });
                return saveFile(byRelatedParam);
            }
        }
        return null;
    }


    @Override
    public List<FileAccessoryEntity> copyByRelatedIdAndModifyRelated(Long copyRelatedId, Long targetRelatedId, String relatedType, String relatedTable, String relatedField) {
        log.info("根据关系ID复制附件信息，并修改附件属性接口接收参数:[copyRelatedId:{},targetRelatedId:{},relatedType:{},relatedTable:{},relatedField:{}]",copyRelatedId,targetRelatedId,relatedType,relatedTable,relatedField);
        if(copyRelatedId != null && targetRelatedId != null){
            List<FileAccessoryEntity> byRelatedParam = findByRelatedParam(null, null, null, copyRelatedId);
            if(CollectionUtils.isNotEmpty(byRelatedParam)) {
                byRelatedParam.forEach(fileAccessoryEntity -> {
                    fileAccessoryEntity.setId(idWorker.nextId());
                    fileAccessoryEntity.setRelatedId(targetRelatedId);
                    fileAccessoryEntity.setRelatedType(relatedType);
                    fileAccessoryEntity.setRelatedTable(relatedTable);
                    if(StringUtils.isNotBlank(relatedField)){
                        fileAccessoryEntity.setRelatedField(relatedField);
                    }
                });
                return saveFile(byRelatedParam);
            }
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> copyByFileIds(Set<Long> fileIds, Long targetRelatedId) {
        log.info("根据附件ID复制附件信息接口接收参数:[fileIds:{},targetRelatedId:{}]",fileIds,targetRelatedId);
        if(CollectionUtils.isNotEmpty(fileIds) && null != targetRelatedId){
            Long[] ids =  fileIds.toArray(new Long[fileIds.size()]);
            List<FileAccessoryEntity> byFileIds = findByFileIds(true,ids);
            if(CollectionUtils.isNotEmpty(byFileIds)) {
                byFileIds.forEach(fileAccessoryEntity -> {
                    fileAccessoryEntity.setId(idWorker.nextId());
                    fileAccessoryEntity.setRelatedId(targetRelatedId);
                });
                return saveFile(byFileIds);
            }
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> copyByFileIdsAndModifyRelated(Set<Long> fileIds, Long targetRelatedId, String relatedType, String relatedTable, String relatedField) {
        log.info("根据关系ID复制附件信息，并修改附件属性接口接收参数:[fileIds:{},targetRelatedId:{},relatedType:{},relatedTable:{},relatedField:{}]",fileIds,targetRelatedId,relatedType,relatedTable,relatedField);
        if(CollectionUtils.isNotEmpty(fileIds) && targetRelatedId != null){
            Long[] ids =  fileIds.toArray(new Long[fileIds.size()]);
            List<FileAccessoryEntity> byFileIds = findByFileIds(true,ids);
            if(CollectionUtils.isNotEmpty(byFileIds)) {
                byFileIds.forEach(fileAccessoryEntity -> {
                    fileAccessoryEntity.setId(idWorker.nextId());
                    fileAccessoryEntity.setRelatedId(targetRelatedId);
                    fileAccessoryEntity.setRelatedType(relatedType);
                    fileAccessoryEntity.setRelatedTable(relatedTable);
                    if(StringUtils.isNotBlank(relatedField)){
                        fileAccessoryEntity.setRelatedField(relatedField);
                    }
                });
                return saveFile(byFileIds);
            }
        }
        return null;
    }

    @Override
    public Map<Long, byte[]> findByteArrayByIds(Long... fileIds) {
        Map<Long,byte[]> result = new HashMap<>();
        if(ArrayUtils.isNotEmpty(fileIds)){
            List<FileAccessoryEntity> byFileIds = findByFileIds(false, fileIds);
            if(CollectionUtils.isNotEmpty(byFileIds)){
                for (FileAccessoryEntity byFileId : byFileIds) {
                    byte[] bytes = null;
                    try {
                        bytes = fastDFSClientWrapper.downloadFile(byFileId.getFilePath());
                    } catch (IOException e) {
                        bytes = new byte[0];
                    }
                    result.put(byFileId.getId(),bytes);
                }
            }
        }
        return result;
    }


    /**
     * 保存附件信息
     * @param files
     * @return
     */
    private List<FileAccessoryEntity> saveFile(List<FileAccessoryEntity> files){
        if(CollectionUtils.isNotEmpty(files)){
            return fileAccessoryService.saveAccessoryFiles(files);
        }
        return null;
    }

    /**
     * 将文件保存到分布式文件服务器中
     * @param multipartFiles 上传文件集合
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @param resolveVidioTime 是否解析视频时间
     * @return 上传后的文件实体列表
     */
    private List<FileAccessoryEntity> transformFiles(MultipartFile[] multipartFiles, String relatedType, String relatedTable, String relatedField, Long relatedId, boolean resolveVidioTime) throws Exception {
        List<FileAccessoryEntity> files = null;
        if(ArrayUtils.isNotEmpty(multipartFiles)){
            files = new ArrayList<>(multipartFiles.length);
            for (MultipartFile file : multipartFiles) {
                //校验文件安全性扩展名等信息，防止恶意信息注入服务器
                FileCheckUtil.checkFileType(file);
                try {
                    byte[] bytes = file.getBytes();
                    String originalFileName = file.getOriginalFilename();
                    String extension = null;
                    if (originalFileName != null) {
                        extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                    }
                    //将文件传输到上传目录下
                    String uploadFile = fastDFSClientWrapper.uploadFile(bytes, file.getSize(), extension);
                    if(StringUtils.isNotBlank(uploadFile)){
                        FileAccessoryEntity.FileAccessoryEntityBuilder fileAccessoryPOBuilder =null; /*fileServiceStrategy.convert2FileAccessory(file,fileAccessoryService.getUserInfo(),idWorker.nextId())
                                .filePath(uploadFile)
                                .relatedType(relatedType)
                                .relatedTable(relatedTable)
                                .relatedField(relatedField)
                                .relatedId(relatedId == null ? 0L : relatedId);*/
                        //如果解析视频时长，则设置视频时间
                        if(resolveVidioTime){
                            fileAccessoryPOBuilder.reserverd(String.valueOf(FileUtil.getVideoTime(file)));
                        }
                        //保存文件
                        files.add(
                                fileAccessoryPOBuilder.build()
                        );
                    }
                    else{
                        log.error("{}文件保存失败",file.getOriginalFilename());
                    }

                } catch (Exception e) {
                    log.error("{}",e);
                    log.error("{}文件保存失败",file.getOriginalFilename());
                }
            }
        }
        return files;
    }

    /**
     * 删除FastDFS中的文件
     * @param files
     * @return
     */
    private Set<Long> delFastDfs(List<FileAccessoryEntity> files) {
        Set<Long> result = null;
        if (CollectionUtils.isNotEmpty(files)) {
            result = new HashSet<>(files.size());
            if (CollectionUtils.isNotEmpty(files)) {
                for (FileAccessoryEntity fileAccessoryDTO : files) {
                    try {
                        fastDFSClientWrapper.deleteFile(fileAccessoryDTO.getFilePath());
                        result.add(fileAccessoryDTO.getId());
                    } catch (Exception e) {
                        log.error("{}文件在FastDFS中删除失败，但在数据库中已经删除", fileAccessoryDTO.getFileName());
                    }
                }
            }
        }
        return result;
    }

}
