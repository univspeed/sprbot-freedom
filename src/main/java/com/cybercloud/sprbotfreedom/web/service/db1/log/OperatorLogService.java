package com.cybercloud.sprbotfreedom.web.service.db1.log;

import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.log.OperateLogEntity;

/**
 * 操作记录业务接口
 * @author liuyutang
 * @date 2023/8/30
 */
public interface OperatorLogService {

    String COLLECTION_NAME = "cmw";

    /**
     * 获取分页列表
     * @param page
     * @param pageSize
     * @param username
     * @param keyword
     * @param startTime
     * @param endTime
     * @param loginIp
     * @return
     */
    PageResult<OperateLogEntity> page(Integer page, Integer pageSize, String username, String keyword, String startTime, String endTime, String loginIp);

    /**
     * 保存操作记录
     * @param description
     * @return
     */
    boolean insert(String description);

    /**
     * 清除日志
     * @param username
     * @param keyword
     * @param startTime
     * @param endTime
     * @param loginIp
     * @return
     */
    boolean del(String username,String keyword,String startTime,String endTime,String loginIp);

}
