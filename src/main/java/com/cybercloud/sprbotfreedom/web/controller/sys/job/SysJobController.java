package com.cybercloud.sprbotfreedom.web.controller.sys.job;

import com.cybercloud.sprbotfreedom.platform.base.controller.BaseController;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.web.entity.bo.IdBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.job.SysJobEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.job.SysJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统任务接口
 * @author liuyutang
 * @date 2023/8/2
 */
@RestController
@RequestMapping("/api/v1/job")
public class SysJobController extends BaseController {

    @Autowired
    private SysJobService sysJobService;

    @GetMapping("/page")
    public PageResult<SysJobEntity> page() {
        return sysJobService.page(
                getPara2Int(DEFAULT_PAGE_NAME,DEFAULT_PAGE),
                getPara2Int(DEFAULT_PAGE_SIZE_NAME,DEFAULT_PAGE_SIZE),
                getPara("name"),
                getPara2Int("jobStatus")
        );
    }

    @GetMapping("/list")
    public List<SysJobEntity> list() {
        return sysJobService.getAllJobs(
                getPara("name"),
                getPara2Int("jobStatus")
        );
    }

    @PostMapping("/update")
    public boolean update(@RequestBody SysJobEntity sysJob) {
        return sysJobService.updateJob(sysJob);
    }

    @PostMapping("/del")
    public boolean del(@RequestBody IdBO idBO) {
        return sysJobService.deleteJobsById(idBO.getId());
    }

}
