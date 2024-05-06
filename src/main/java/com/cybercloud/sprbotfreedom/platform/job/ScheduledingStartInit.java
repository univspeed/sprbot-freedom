package com.cybercloud.sprbotfreedom.platform.job;

import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.job.SysJobEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.job.SysJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动初始化定时任务
 * @author liuyutang
 * @date 2023/8/2
 */
@Slf4j
@Component
public class ScheduledingStartInit implements CommandLineRunner {

    @Autowired
    private CronTaskRegister cronTaskRegister;
    @Autowired
    private SysJobService sysJobService;

    @Override
    public void run(String... args) throws Exception {
        List<SysJobEntity> allJobs = sysJobService.getAllJobs(null, 1);
        log.info(">>> Start job scheduling task queue loading jobs num : {}" ,allJobs.size());
        if(CollectionUtils.isNotEmpty(allJobs)){
            for (SysJobEntity job : allJobs) {
                cronTaskRegister.addCronTask(new SchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getMethodParams()),job.getCronExpression());
            }
        }
    }

}
