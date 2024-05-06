package com.cybercloud.sprbotfreedom.platform.file.strategy.impl;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.file.FileService;
import com.cybercloud.sprbotfreedom.platform.file.strategy.FileServiceStrategy;
import com.cybercloud.sprbotfreedom.platform.util.CacheUtil;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.file.FileAccessoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 文件上传策略业务接口实现
 * @author liuyutang
 */
@Service
@Slf4j
@EnableScheduling
public class FileServiceStrategyImpl implements FileServiceStrategy {

    @Value("${system.file-system-type:fastDFS}")
    private String key;

    private final FileAccessoryService fileAccessoryService;


    /**
     * 存储策略名称与策略具体实现
     * Key:策略名称 Value：策略具体实现
     */
    private static final Map<String,FileService> STRATEGY_MAP = new ConcurrentHashMap<>();

    public FileServiceStrategyImpl(FileAccessoryService fileAccessoryService) {
        this.fileAccessoryService = fileAccessoryService;
    }

    @Override
    public FileAccessoryEntity upload(MultipartFile file, String relatedType, String relatedTable, String relatedField, Long relatedId, String forceSystemType, boolean resolveVidioTime) throws Exception {
        String currentKey = getCurrentKey(forceSystemType);
        FileService fileService = STRATEGY_MAP.get(currentKey);
        if(fileService != null){
            return fileService.upload(file,relatedType,relatedTable,relatedField,relatedId,resolveVidioTime);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> upload(MultipartFile[] files, String relatedType, String relatedTable, String relatedField, Long relatedId, String forceSystemType, boolean resolveVidioTime) throws Exception {
        String currentKey = getCurrentKey(forceSystemType);
        FileService fileService = STRATEGY_MAP.get(currentKey);
        if(fileService != null){
            return fileService.upload(files,relatedType,relatedTable,relatedField,relatedId,resolveVidioTime);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByPath(String... filePaths) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.findByPath(filePaths);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.findByRelatedParam(relatedType, relatedTable, relatedField, relatedId);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> findByFileIds(boolean relatedIdNull,Long... fileIds) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.findByFileIds(relatedIdNull,fileIds);
        }
        return null;
    }

    @Override
    public Set<Long> logicDelFileByIds(Long... fileIds) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.logicDelFileByIds(fileIds);
        }
        return null;
    }

    @Override
    public Set<Long> logicDelFileByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.logicDelFileByRelatedParam(relatedType,relatedTable,relatedField,relatedId);
        }
        return null;
    }

    @Override
    public Set<Long> delFileByIds(String forceSystemType,Long... fileIds) {
        String currentKey = getCurrentKey(forceSystemType);
        FileService fileService = STRATEGY_MAP.get(currentKey);
        if(fileService != null){
            return fileService.delFileByIds(fileIds);
        }
        return null;
    }

    @Override
    public Set<Long> delFileByRelatedParam(String relatedType, String relatedTable, String relatedField, Long relatedId,String forceSystemType) {
        String currentKey = getCurrentKey(forceSystemType);
        FileService fileService = STRATEGY_MAP.get(currentKey);
        if(fileService != null){
            return fileService.delFileByRelatedParam(relatedType,relatedTable,relatedField,relatedId);
        }
        return null;
    }

    @Override
    public Set<Long> bindFileAndDataId(Set<Long> fileIds, String relatedType, String relatedTable, Long relatedId) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.bindFileAndDataId(fileIds,relatedType,relatedTable,relatedId);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> copyByRelatedId(Long copyRelatedId, Long targetRelatedId) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.copyByRelatedId(copyRelatedId,targetRelatedId);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> copyByRelatedIdAndModifyRelated(Long copyRelatedId, Long targetRelatedId, String relatedType, String relatedTable, String relatedField) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.copyByRelatedIdAndModifyRelated(copyRelatedId,targetRelatedId,relatedType,relatedTable,relatedField);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> copyByFileIds(Set<Long> fileIds, Long targetRelatedId) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.copyByFileIds(fileIds,targetRelatedId);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> copyByFileIdsAndModifyRelated(Set<Long> fileIds, Long targetRelatedId, String relatedType, String relatedTable, String relatedField) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.copyByFileIdsAndModifyRelated(fileIds,targetRelatedId,relatedType,relatedTable,relatedField);
        }
        return null;
    }

    @Override
    public List<FileAccessoryEntity> saveFiles(List<FileAccessoryEntity> files) {
        if(CollectionUtils.isNotEmpty(files)){
            return fileAccessoryService.saveAccessoryFiles(files);
        }
        return null;
    }

    @Override
    public Map<Long,byte[]> findByteArrayByIds(Long... fileIds) {
        FileService fileService = STRATEGY_MAP.get(key);
        if(fileService != null){
            return fileService.findByteArrayByIds(fileIds);
        }
        return null;
    }

    @Override
    public void register(String key, FileService fileService) {
        STRATEGY_MAP.put(key,fileService);
    }

    @Override
    public void unInstall(String key) {
        STRATEGY_MAP.remove(key);
    }

    @Override
    public String getCurrentKey(String forceSystemType) {
        if(StringUtils.isNotBlank(forceSystemType)){return forceSystemType;}
        return this.key;
    }

    @Override
    public int countDownUploadNum(UserInfo userInfo, int num) {
        if(getUploadNumLimitSwitch()){
            final Lock lock = new ReentrantLock();
            lock.lock();
            try {
                //未登录上传
                if(userInfo==null){
                    Integer unloginUploadNumLimit  = Integer.valueOf((String) CacheUtil.get(UNLOGIN_UPLOAD_NUM_LIMIT));
                    if(unloginUploadNumLimit != null && unloginUploadNumLimit > 0){
                        Integer countDown = unloginUploadNumLimit - 1;
                        CacheUtil.set(UNLOGIN_UPLOAD_NUM_LIMIT,(countDown).toString());
                        log.info("扣减< {} >上传附件次数，剩余次数：< {} >",UNLOGIN_UPLOAD_NUM_LIMIT,countDown);
                        return countDown;
                    }else{
                        log.error("未登录用户单日上传数量超出限制，请联系运维人员扩容。");
                        ServiceException.throwError(SystemErrorCode.ERROR_90000);
                        return 0;
                    }
                }
                else{
                    String key = String.format("%s_%s",userInfo.getId(),LOGIN_UPLOAD_NUM_LIMIT);
                    Integer loginUploadNumLimit  = Integer.valueOf((String) CacheUtil.get(key));
                    if(loginUploadNumLimit != null && loginUploadNumLimit > 0){
                        Integer countDown = loginUploadNumLimit - 1;
                        CacheUtil.set(key,countDown.toString());
                        log.info("扣减< {} >上传附件次数，剩余次数：< {} >",userInfo.getUsername(),countDown);
                        return countDown;
                    }else{
                        log.error(String.format("< %s >用户单日上传数量超出限制，请联系运维人员扩容。",userInfo.getUsername()));
                        ServiceException.throwError(SystemErrorCode.ERROR_90000);
                        return 0;
                    }
                }
            }finally {
                lock.unlock();
            }
        }else{
            return 0;
        }
    }

    @Override
    public void extendCapacity(UserInfo userInfo, int num) {
        if(getUploadNumLimitSwitch()){
            //未登录用户扩容
            if(userInfo == null){
                // 获取当前次数
                Integer unloginUploadNumLimit  = Integer.valueOf((String) CacheUtil.get(UNLOGIN_UPLOAD_NUM_LIMIT));
                // 获取需要扩容的数量
                Integer extendCapacity = getExtendCapacity(UNLOGIN_UPLOAD_NUM_LIMIT, num) + unloginUploadNumLimit;
                //扩容
                CacheUtil.set(UNLOGIN_UPLOAD_NUM_LIMIT,extendCapacity.toString());
                log.info("扩展< {} >上传附件次数，当前次数< {} >，剩余次数< {} >",UNLOGIN_UPLOAD_NUM_LIMIT,unloginUploadNumLimit,extendCapacity);
            }
            //已登录用户扩容
            else{
                String key = String.format("%s_%s",userInfo.getId(),LOGIN_UPLOAD_NUM_LIMIT);
                // 获取当前次数
                Integer loginUploadNumLimit  = Integer.valueOf((String) CacheUtil.get(key));
                // 获取需要扩容的数量
                Integer extendCapacity = getExtendCapacity(LOGIN_UPLOAD_NUM_LIMIT, num) + loginUploadNumLimit;
                //扩容
                CacheUtil.set(key,extendCapacity.toString());
                log.info("扩展< {} >上传附件次数，当前次数< {} >，剩余次数< {} >",key,loginUploadNumLimit,extendCapacity);
            }
        }
    }

    @Override
    @Scheduled(cron = "59 59 23 * * ?")
    public void initCapacity() {
        /*if(getUploadNumLimitSwitch()){
            log.info(">>> Start init file upload limit num.");
            //初始化未登录上传附件数量限制
            CacheUtil.set(UNLOGIN_UPLOAD_NUM_LIMIT,getExtendCapacity(UNLOGIN_UPLOAD_NUM_LIMIT,0).toString());
            log.info("Unlogin user init file upload limit num done.");
            List<Long> allUserIds = getAllUserIds();
            if(CollectionUtils.isNotEmpty(allUserIds)){
                for (Long allUserId : allUserIds) {
                    String key = String.format("%s_%s",allUserId,LOGIN_UPLOAD_NUM_LIMIT);
                    CacheUtil.set(key,getExtendCapacity(LOGIN_UPLOAD_NUM_LIMIT,0).toString());
                }
                log.info("Logined user init file upload limit num done.");
            }
        }*/
    }

    @Override
    public boolean getUploadNumLimitSwitch() {
        return fileAccessoryService.loadBykey(UPLOAD_NUM_LIMIT_SWITCH,Boolean.class);
    }

    /**
     * 获取扩容数量
     * @param cKey key只能只用 UNLOGIN_UPLOAD_NUM_LIMIT | UNLOGIN_UPLOAD_NUM_LIMIT
     * @param num
     * @return
     */
    private Integer getExtendCapacity(String cKey,int num){
        Integer cvalue = fileAccessoryService.loadBykey(cKey, Integer.class);
        Integer configCapaticy = cKey.equals(UNLOGIN_UPLOAD_NUM_LIMIT) ? 300 : 100;
        if (cvalue != null && cvalue != 0) {
            configCapaticy = cvalue;
        }
        if(num == 0 || num > configCapaticy){
            return configCapaticy;
        }
        return num;
    }

    /**
     * 获取所有用户id集合
     * @return
     */
    /*private List<Long> getAllUserIds(){
        List<SysUserEntity> all = sysUserService.findAll(null);
        if(CollectionUtils.isNotEmpty(all)){
            return all.stream().map(SysUserEntity::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }*/
}
