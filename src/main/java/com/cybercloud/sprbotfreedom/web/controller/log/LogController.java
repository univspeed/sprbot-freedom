package com.cybercloud.sprbotfreedom.web.controller.log;

import com.cybercloud.sprbotfreedom.platform.base.controller.BaseController;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.web.entity.bo.log.OperatorLogDelBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.log.OperateLogEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.log.OperatorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuyutang
 * @date 2023/8/30
 */
@RestController
@RequestMapping("/api/v1/log")
public class LogController extends BaseController {

    @Autowired
    private OperatorLogService operatorLogService;

    @GetMapping("page")
    public PageResult<OperateLogEntity> page(){
        return operatorLogService.page(
                getPara2Int(DEFAULT_PAGE_NAME,DEFAULT_PAGE),
                getPara2Int(DEFAULT_PAGE_SIZE_NAME,DEFAULT_PAGE_SIZE),
                getPara("username"),
                getPara("keywork"),
                getPara("startTime"),
                getPara("endTime"),
                getPara("loginIp")
        );
    }

    @PostMapping("del")
    public boolean del(@RequestBody OperatorLogDelBO operatorLogDelBO){
        return operatorLogService.del(
                operatorLogDelBO.getUsername(),
                operatorLogDelBO.getKeywork(),
                operatorLogDelBO.getStartTime(),
                operatorLogDelBO.getEndTime(),
                operatorLogDelBO.getLoginIp()
        );
    }
}
