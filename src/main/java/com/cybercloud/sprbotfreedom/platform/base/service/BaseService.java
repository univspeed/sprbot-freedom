package com.cybercloud.sprbotfreedom.platform.base.service;

import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.FileRelatedEnum;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 业务层基本方法接口
 * @author liuyutang
 */
public interface BaseService<E extends BaseEntity> {


    /**
     * 获取当前登录用户的用户信息
     * @return {{@link UserInfo}}
     */
    UserInfo getUserInfo();

    /**
     * 获取请求客户端ip
     * @return
     */
    String getRequestClientIp();

    /**
     * 组装动态表单链接
     * @param dyMethodUrl 动态表单方法路径
     * @param alias 别名
     * @param params 参数列表
     * @return
     */
    String getUrl(String dyMethodUrl, String alias, Map<String, Object> params);

    /**
     * 默认逻辑删除方法
     * @param ids 需要删除的id集合
     * @return
     */
    Set<Long> defaultLogicDelete(Set<Long> ids);

    /**
     * 未登录删除
     * @param ids
     * @return
     */
    Set<Long> defaultLogicDeleteUnLogin(Set<Long> ids);

    /**
     * 默认逻辑删除方法
     * @param ids 需要删除的id集合
     * @return
     */
    Set<Long> defaultLogicDelete(Long... ids);
    /**
     * 默认逻辑删除方法
     * @param es 需要删除的对象集合
     * @return
     */
    Set<Long> defaultLogicDelete(List<E> es);
    /**
     * 默认逻辑删除方法
     * @param ids 需要删除的id集合
     * @return
     */
    Set<Long> defaultLogicDeleteUnLogin(Long... ids);
    /**
     * 未登录删除方法
     * @param es 需要删除的对象集合
     * @return
     */
    Set<Long> defaultLogicDeleteUnLogin(List<E> es);
    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    E defaultUpdate(E e);

    /**
     * 未登录更新
     * @param e
     * @param delete
     * @return
     */
    E defaultUpdateUnLogin(E e, boolean delete);

    /**
     * 未登录更新方法
     * @param e
     * @return
     */
    E defaultUpdateUnLogin(E e);

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    List<E> defaultUpdate(List<E> e);

    /**
     * 未登录更新方法
     * @param e
     * @return
     */
    List<E> defaultUpdateUnLogin(List<E> e);

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @param delete 是否需要逻辑删除
     * @return
     */
    E defaultUpdate(E e, boolean delete);

    /**
     * 只更新传入实体内容，不自动设置更新用户以及时间
     * @param e
     * @param delete
     * @return
     */
    E defaultUpdateSingle(E e, boolean delete);

    /**
     * 默认保存方法
     * @param e
     * @param snow
     * @return
     */
    E defaultSave(E e, boolean snow);

    /**
     * 未登录保存方法
     * @param e
     * @param snow
     * @return
     */
    E defaultSaveUnLogin(E e,boolean snow);

    /**
     * 默认保存方法
     * @param es
     * @param snow
     * @return
     */
    List<E> defaultSave(List<E> es,boolean snow);

    /**
     * 未登录保存方法
     * @param es
     * @param snow
     * @return
     */
    List<E> defaultSaveUnLogin(List<E> es,boolean snow);

    /**
     * 获取Post请求Body中的参数值
     * @param paramName
     * @return
     */
    Object getBodyParam(String paramName);

    /**
     * 默认查看详情
     * @param id
     * @return
     */
    E defaultFindOne(Long id);

    /**
     * 默认查看详情
     * @param id
     * @param convert 需要转变的类型
     * @param <T>
     * @return
     */
    <T> T defaultFindOne(Long id, Class<T> convert);

    /**
     * 默认批量查找
     * @param ids
     * @return
     */
    Map<Long, E> defaultFindByIds(Set<Long> ids);
    /**
     * 默认批量查找
     * @param ids
     * @return
     */
    Map<Long, E> defaultFindByIds(Long... ids);

    /**
     * 默认查找所有
     * @return
     */
    List<E> defaultFindAll();

    /**
     * 获取文件信息
     * @param fileRelatedEnum
     * @param relatedField
     * @param relatedId
     * @return
     */
    List<FileAccessoryEntity> getFilesByParam(FileRelatedEnum fileRelatedEnum, String relatedField, Long relatedId);
    /**
     * 更新上传文件信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     * @param requestFileIdName 前端传递的附件参数名称
     */
    Set<Long> bindFile(Long relatedId, FileRelatedEnum fileRelatedEnum, String... requestFileIdName);
    /**
     * 更新上传文件信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     * @param fileIds 附件id集合
     */
    Set<Long> bindFile(Long relatedId, FileRelatedEnum fileRelatedEnum, List<Long> fileIds);

    /**
     * 根据前端传递的文件名称获取文件ID
     * @param requestFileIdName
     * @return
     */
    List<Long> getFileIds(String... requestFileIdName);
}
