package com.cybercloud.sprbotfreedom.web.service.db1.sys.job;

import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.job.SysJobEntity;

import java.util.List;

/**
 * 定时任务业务接口
 * @author liuyutang
 * @date 2023/8/2
 */
public interface SysJobService extends BaseService<SysJobEntity> {

    /**
     * 任务分页列表
     * @param page
     * @param pageSize
     * @param name
     * @param jobStatus
     * @return
     */
    PageResult<SysJobEntity> page(Integer page,Integer pageSize,String name,Integer jobStatus);

    /**
     * 任务列表
     * @param name
     * @param jobStatus
     * @return
     */
    List<SysJobEntity> getAllJobs(String name,Integer jobStatus);

    /**
     * 添加任务
     * @param sysJob
     * @return
     */
    boolean addJob(SysJobEntity sysJob);

    /**
     * 更新任务
     * @param sysJob
     * @return
     */
    boolean updateJob(SysJobEntity sysJob);

    /**
     * 删除任务
     * @param id
     * @return
     */
    boolean deleteJobsById(Long id);
}
