package com.cybercloud.sprbotfreedom.web.controller.sys.user;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.controller.BaseController;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.web.entity.bo.IdBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.user.ModifyPassBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.user.SaveUserBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.user.SysUserEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.user.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作员接口
 * @author liuyutang
 * @date 2023/7/11
 */
@PrintFunctionLog
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("page")
    public PageResult<SysUserEntity> page(){

        return sysUserService.page(
                getPara2Int(DEFAULT_PAGE_NAME,DEFAULT_PAGE),
                getPara2Int(DEFAULT_PAGE_SIZE_NAME,DEFAULT_PAGE_SIZE),
                getPara("name")
        );
    }

    @GetMapping("list")
    public List<SysUserEntity> list(){

        return sysUserService.findAll(getPara("name"));
    }

    @GetMapping("info/{id}")
    public SysUserEntity info(@PathVariable("id") Long id){
        Assert.notNull(id, "查询详情id不能为空");
        return sysUserService.findById(id);
    }

    @PostMapping("save")
    public SysUserEntity save(@RequestBody SaveUserBO user){
        Assert.notNull(user, "新增操作员对象不能为空");
        return sysUserService.add(user);
    }

    @PostMapping("update")
    public SysUserEntity update(@RequestBody SaveUserBO user){
        Assert.notNull(user, "修改操作员对象不能为空");
        Long id = user.getId();
        Assert.notNull(id, "修改操作员对象ID不能为空");
        return sysUserService.update(user);
    }

    @PostMapping("del")
    public boolean del(@RequestBody IdBO idBO){
        Long id = idBO.getId();
        Assert.notNull(id,"删除操作员时ID不能为空");
        return sysUserService.del(id);
    }

    @PostMapping("modify_pass")
    public boolean modifyPass(@RequestBody ModifyPassBO modifyPassBO){
        Assert.notNull(modifyPassBO.getId(),"操作员ID不能为空");
        Assert.notNull(modifyPassBO.getOldPass(),"原密码不能为空");
        Assert.notNull(modifyPassBO.getNewPass(),"新密码不能为空");
        Assert.notNull(modifyPassBO.getComfirmPass(),"确认密码不能为空");
        return sysUserService.modifyPassword(modifyPassBO);
    }
}
