package com.cybercloud.sprbotfreedom.platform.file.strategy;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.file.FileService;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传策略接口
 * @author liuyutang
 */
public interface FileServiceStrategy {

    /**
     * 本地文件上传
     */
    String LOCAL = "local";

    /**
     * 分布式文件服务器
     */
    String FAST_DFS = "fastDFS";

    /**
     *  minIo
     */
    String MINIO="minIo";

    /**
     *  oss
     */
    String OSS="oss";
    /**
     * 未登录用户上传次数限制RedisKey
     */
    String UNLOGIN_UPLOAD_NUM_LIMIT = "unloginUploadNumLimit";
    /**
     * 未登录用户上传次数限制RedisKey
     */
    String LOGIN_UPLOAD_NUM_LIMIT = "loginUploadNumLimit";

    /**
     * 校验附件上传数量限制开关
     */
    String UPLOAD_NUM_LIMIT_SWITCH = "uploadNumLimitSwitch";

    /**
     * 上传单个文件
     * 文件保存逻辑：如果relatedId为空，则直接保存，否则先删除relatedId关联的所有文件，然后在保存到t_file_accessory
     * @param file 文件流
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @param forceSystemType 强制上传系统类型
     * @param resolveVidioTime 是否解析视频时长
     * @return 上传后的文件信息
     */
    FileAccessoryEntity upload(MultipartFile file, String relatedType, String relatedTable, String relatedField, Long relatedId, String forceSystemType, boolean resolveVidioTime) throws Exception;

    /**
     * 上传多个文件
     * 文件保存逻辑：如果relatedId为空，则直接保存，否则先删除relatedId关联的所有文件，然后在保存到t_file_accessory
     * @param files 文件流数组
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @param forceSystemType 强制上传系统类型
     * @param resolveVidioTime 是否解析视频时长
     * @return 上传后的文件信息列表
     */
    List<FileAccessoryEntity> upload(MultipartFile[] files, String relatedType, String relatedTable, String relatedField, Long relatedId, String forceSystemType, boolean resolveVidioTime) throws Exception;

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
     * @param fileIds 文件id数组
     * @param relatedIdNull 是否过滤relatedId为空的数据
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
     * @param forceSystemType 强制上传类型
     * @param fileIds 文件id数组
     * @return
     */
    Set<Long> delFileByIds(String forceSystemType,Long... fileIds);

    /**
     * 批量删除文件（物理删除）
     * @param relatedType  模块
     * @param relatedTable 表名
     * @param relatedField 字段名
     * @param relatedId 关联数据id
     * @param forceSystemType 强制上传类型
     * @return
     */
    Set<Long> delFileByRelatedParam(String relatedType,String relatedTable,String relatedField,Long relatedId,String forceSystemType);

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
     * 保存附件信息，用于各种数据同步携带附件的场景，解决不走上传，直接保存的问题
     * @param files 需要保存的文件信息列表
     * @return 保存成功的文件信息列表
     */
    List<FileAccessoryEntity> saveFiles(List<FileAccessoryEntity> files);

    /**
     * 根据附件ID获取字节数组
     * @param fileIds 保存成功的附件ID数组
     * @return 文件的字节数组
     */
    Map<Long,byte[]> findByteArrayByIds(Long... fileIds);

    /**
     * 将MultipartFile基本信息填入实体
     * filePath 需要在上传到文件服务器之后添加
     * relatedType 需要在前端传入后端服务添加
     * relatedTable 需要在前端传入后端服务添加
     * relatedField 需要在前端传入后端服务添加
     * @param file
     * @return
     */
    /*default FileAccessoryEntity.FileAccessoryEntityBuilder convert2FileAccessory(MultipartFile file, UserInfo userInfo, long id){
        if(file != null){
            String dateTimeNoFormat = DateUtil.getDateTimeNoFormat();
            return FileAccessoryEntity.builder()
                    .id(id)
                    .createTime(dateTimeNoFormat)
                    .createUser(userInfo==null?null:userInfo.getUsername())
                    .updateTime(dateTimeNoFormat)
                    .updateUser(userInfo==null?null:userInfo.getUsername())
                    .fileName(file.getOriginalFilename())
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .state(1)
                    .syscode(null);
        }
        return null;
    }*/

    /**
     * 注册策略
     * @param key 策略唯一标识
     * @param fileService 文件服务策略实现
     */
    void register(String key, FileService fileService);

    /**
     * 删除策略
     * @param key 策略唯一标识
     */
    void unInstall(String key);

    /**
     * 获取当前启动的上传模式
     * @param forceSystemType 强制上传类型，如果传递则返回此类型
     * @return
     */
    String getCurrentKey(String forceSystemType);

    /**
     * 向下计数附件上传次数
     * <p>1、计数规则，调用单附件上传扣除一次，调用多个附件上传扣除包含附件个数次数</p>
     * <p>2、次数限制，当个数扣减为0时，则无法上传附件</p>
     * @param userInfo 传递用户信息，则扣减对应用户附件上传次数，否则扣减未登录上传次数
     * @param num 扣减数量
     * @return 剩余数量
     */
    int countDownUploadNum(UserInfo userInfo,int num);

    /**
     * 用户附件限制扩容接口
     * <p>1、扩展上传次数容量，未登录上传数量扩容单次不得超过300，登录用户上传数量扩容单次不得超过100</p>
     * <p>2、如果扩容数量为0，则按默认配置扩容 未登录上传数量：单日300，已登录用户上传数量：单日100</p>
     * @param userInfo 传递用户信息，则扩容对应用户附件上传次数，否则扩容未登录上传次数
     * @param num 扩容数量
     */
    void extendCapacity(UserInfo userInfo,int num);

    /**
     * 初始化上传容量接口
     * <p>1、初始化容量读取系统配置（sys_config） 未用户上传（unloginUploadNumLimit）以及登录用户（loginUploadNumLimit）</p>
     * <p>2、未配置，则只用默认配置，未登录上传数量：单日300，已登录用户上传数量：单日100</p>
     * <p>3、Redis缓存key，未登录用户缓存Key（unloginUploadNumLimit）、登录用户缓存key（用户名_loginUploadNumLimit）分开配置并再Redis中初始化</p>
     * <p>4、每日初始化上传数量缓存配置</p>
     */
    void initCapacity();

    /**
     * 校验附件上传数量限制开关状态
     * @return
     */
    boolean getUploadNumLimitSwitch();
}
