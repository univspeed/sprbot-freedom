package com.cybercloud.sprbotfreedom.web.controller.sys.resource;

import com.cybercloud.sprbotfreedom.platform.base.controller.BaseController;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.web.entity.bo.IdBO;
import com.cybercloud.sprbotfreedom.web.entity.bo.resource.RoleResourceBO;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleResourceEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.resource.SysResourceService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源接口
 * @author liuyutang
 * @date 2023/7/12
 */
@RestController
@RequestMapping("/api/v1/resource")
public class SysResourceController extends BaseController {

    @Autowired
    private SysResourceService sysResourceService;
    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @GetMapping("page")
    public PageResult<SysResourceEntity> page(){

        return sysResourceService.page(
                getPara2Int(DEFAULT_PAGE_NAME,DEFAULT_PAGE),
                getPara2Int(DEFAULT_PAGE_SIZE_NAME,DEFAULT_PAGE_SIZE),
                getPara("name"),
                getPara("type"),
                getPara2Long("parent")
        );
    }

    @GetMapping("list")
    public List<SysResourceEntity> list(){

        return sysResourceService.findAll(
                getPara("name"),
                getPara("type"),
                getPara2Long("parent")
        );
    }

    @GetMapping("info/{id}")
    public SysResourceEntity info(@PathVariable("id") Long id){
        Assert.notNull(id, "查询详情id不能为空");
        return sysResourceService.findById(id);
    }

    @PostMapping("save")
    public SysResourceEntity save(@RequestBody SysResourceEntity resource){
        Assert.notNull(resource, "新增资源对象不能为空");
        Assert.notNull(resource.getResourceCode(),"资源编码不能为空");
        Assert.notNull(resource.getResourceType(),"资源类型不能为空");
        return sysResourceService.add(resource);
    }

    @PostMapping("update")
    public SysResourceEntity update(@RequestBody SysResourceEntity resource){
        Assert.notNull(resource, "修改资源对象不能为空");
        Long id = resource.getId();
        Assert.notNull(id, "修改资源对象ID不能为空");
        return sysResourceService.update(resource);
    }

    @PostMapping("del")
    public boolean del(@RequestBody IdBO idBO){
        Long id = idBO.getId();
        Assert.notNull(id,"删除资源时ID不能为空");
        return sysResourceService.del(id);
    }

    @PostMapping("disable")
    public boolean disable(@RequestBody IdBO idBO){
        Long id = idBO.getId();
        Assert.notNull(id,"禁用资源时ID不能为空");
        return sysResourceService.disable(id);
    }

    @PostMapping("bind_role")
    public List<SysRoleResourceEntity> disable(@RequestBody RoleResourceBO resourceBO){
        Assert.notNull(resourceBO,"绑定资源角色对象不能为空");
        Assert.notNull(resourceBO.getResourceCode(),"绑定资源角色ID不能为空");
        return sysRoleResourceService.add(resourceBO);
    }

    @GetMapping("role_resource")
    public List<SysResourceEntity> roleResource(){
        return sysRoleResourceService.findResByRoleId(
                getPara2Long("roleId"),
                getPara("type"),
                getPara2Bool("enable")
        );
    }
}
