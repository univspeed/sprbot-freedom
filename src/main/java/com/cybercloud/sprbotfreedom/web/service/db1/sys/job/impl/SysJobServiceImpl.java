package com.cybercloud.sprbotfreedom.web.service.db1.sys.job.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.job.CronTaskRegister;
import com.cybercloud.sprbotfreedom.platform.job.SchedulingRunnable;
import com.cybercloud.sprbotfreedom.web.dao.db1.sys.job.SysJobDAO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.job.SysJobEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.job.SysJobService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务业务接口实现
 * @author liuyutang
 * @date 2023/8/2
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class SysJobServiceImpl extends BaseServiceImpl<SysJobEntity, SysJobDAO> implements SysJobService {

    private final CronTaskRegister cronTaskRegister;

    public SysJobServiceImpl(CronTaskRegister cronTaskRegister) {
        this.cronTaskRegister = cronTaskRegister;
    }

    @Override
    public PageResult<SysJobEntity> page(Integer page, Integer pageSize, String name, Integer jobStatus) {
        Page<SysJobEntity> sysJobEntityPage = dao.selectPage(getEmptyPage(page, pageSize),
                normalStateLambdaQueryWrapper().lambda()
                    .like(StringUtils.isNotBlank(name), SysJobEntity::getMethodName, name)
                    .eq(jobStatus != null, SysJobEntity::getJobStatus, jobStatus)
        );
        return PageResult.create(sysJobEntityPage);
    }

    @Override
    public List<SysJobEntity> getAllJobs(String name, Integer jobStatus) {
        return dao.selectList(
                normalStateLambdaQueryWrapper().lambda()
                    .like(StringUtils.isNotBlank(name), SysJobEntity::getMethodName, name)
                    .eq(jobStatus != null, SysJobEntity::getJobStatus, jobStatus)
        );
    }

    @Override
    public boolean addJob(SysJobEntity sysJob) {
        SysJobEntity sysJobEntity = dao.selectOne(
                normalStateLambdaQueryWrapper().lambda()
                        .eq(SysJobEntity::getBeanName, sysJob.getBeanName())
                        .eq(SysJobEntity::getMethodName, sysJob.getMethodName())
        );
        if(sysJobEntity != null){
            ServiceException.throwError(SystemErrorCode.ERROR_20000);
        }
        SysJobEntity save = defaultSave(sysJob,true);
        if(save != null){
            SchedulingRunnable schedulingRunnable = new SchedulingRunnable(save.getBeanName(), save.getMethodName(), save.getMethodParams());
            if(save.getJobStatus() == 1){
                cronTaskRegister.addCronTask(schedulingRunnable, sysJob.getCronExpression());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJob(SysJobEntity sysJob) {
        SysJobEntity job = defaultUpdate(sysJob);
        if (job != null) {
            SchedulingRunnable schedulingRunnable = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
            if (sysJob.getJobStatus() == 1) {
                cronTaskRegister.addCronTask(schedulingRunnable, sysJob.getCronExpression());
            } else {
                cronTaskRegister.removeCronTask(schedulingRunnable);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteJobsById(Long id) {
        return CollectionUtils.isNotEmpty(defaultLogicDelete(id));
    }
}
