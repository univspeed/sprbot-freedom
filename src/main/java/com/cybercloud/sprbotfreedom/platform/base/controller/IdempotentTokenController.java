package com.cybercloud.sprbotfreedom.platform.base.controller;


import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.IdempotentTokenEntity;
import com.cybercloud.sprbotfreedom.platform.base.service.IdempotentTokenService;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevel;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevelRetention;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自动幂等性校验Token操作接口
 *
 * @author liuyutang
 */
@Api(value="幂等性校验Token操作接口",tags={"幂等性校验Token操作"})
@RestController
@RequestMapping("/api/v1/idempotent")
@Validated
@PrintFunctionLog(level = PrintLevel.DEBUG,retention = PrintLevelRetention.UP)
public class IdempotentTokenController extends BaseController{

    @Autowired
    private IdempotentTokenService idempotentTokenService;

    @GetMapping(value = "/token")
    @ApiOperation(value="获取幂等性校验token",tags={"幂等性校验Token操作"},notes="")
    public IdempotentTokenEntity getToken(){

        return idempotentTokenService.getToken();
    }
}
