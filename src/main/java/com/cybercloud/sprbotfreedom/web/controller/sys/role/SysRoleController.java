package com.cybercloud.sprbotfreedom.web.controller.sys.role;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.controller.BaseController;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.web.entity.bo.IdBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色接口
 * @author liuyutang
 * @date 2023/7/11
 */
@PrintFunctionLog
@RestController
@RequestMapping("/api/v1/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("page")
    public PageResult<SysRoleEntity> page(){

        return sysRoleService.page(
                getPara2Int(DEFAULT_PAGE_NAME,DEFAULT_PAGE),
                getPara2Int(DEFAULT_PAGE_SIZE_NAME,DEFAULT_PAGE_SIZE),
                getPara("roleName")
        );
    }

    @GetMapping("list")
    public List<SysRoleEntity> list(){

        return sysRoleService.findAll(getPara("roleName"));
    }

    @GetMapping("info/{id}")
    public SysRoleEntity info(@PathVariable("id") Long id){
        Assert.notNull(id, "查询详情id不能为空");
        return sysRoleService.findById(id);
    }

    @PostMapping("save")
    public SysRoleEntity save(@RequestBody SysRoleEntity role){
        Assert.notNull(role, "新增角色对象不能为空");
        return sysRoleService.add(role);
    }

    @PostMapping("update")
    public SysRoleEntity update(@RequestBody SysRoleEntity role){
        Assert.notNull(role, "修改角色对象不能为空");
        Long id = role.getId();
        Assert.notNull(id, "修改角色对象ID不能为空");
        return sysRoleService.update(role);
    }

    @PostMapping("del")
    public boolean del(@RequestBody IdBO idBO){
        Long id = idBO.getId();
        Assert.notNull(id,"删除角色时ID不能为空");
        return sysRoleService.del(id);
    }
}
