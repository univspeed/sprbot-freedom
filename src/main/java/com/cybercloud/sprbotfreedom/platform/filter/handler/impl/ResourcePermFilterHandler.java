package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import com.cybercloud.sprbotfreedom.platform.util.CacheUtil;
import com.cybercloud.sprbotfreedom.platform.util.JwtUtil;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.role.SysRoleResourceEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.resource.SysResourceService;
import com.cybercloud.sprbotfreedom.web.service.db1.sys.role.SysRoleResourceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源权限处理器
 * @author liuyutang
 * @date 2023/7/11
 */
@Slf4j
@Component("resourcePermHandler")
public class ResourcePermFilterHandler implements FilterHandler {


    @Value("${system.resource-permission.open-permission-check:false}")
    public boolean openResPermProtect;
    @Value("${system.auth-check.open-auth-check:true}")
    private boolean openAuthFilter;
    @Value("${system.token.header:authorization}")
    private String headerKey;
    @Value("${system.resource-permission.resouce-white-list:}")
    private String whiteListUrls;
    private final FilterHandlerChain filterHandlerChain;
    private final SysRoleResourceService sysRoleResourceService;
    private final SysResourceService sysResourceService;
    private static final String CACHE_PERMISSION_KEY = "user-%s";
    private static final String CACHE_PERMISSION_SUB_KEY = "permission";

    public ResourcePermFilterHandler(FilterHandlerChain filterHandlerChain, SysRoleResourceService sysRoleResourceService, SysResourceService sysResourceService) {
        this.filterHandlerChain = filterHandlerChain;
        this.sysRoleResourceService = sysRoleResourceService;
        this.sysResourceService = sysResourceService;
    }

    @Override
    public void handle() throws IOException {
        if (openResPermProtect && openAuthFilter) {
            log.info(">>>>>>>>> 资源权限过滤器就绪");
            HttpServletRequest request = ((HttpServletRequest) filterHandlerChain.getRequest());
            String requestUri = request.getRequestURI();
            if(StringUtils.isNotBlank(requestUri)){
                if(white(whiteListUrls,requestUri)){
                    log.info("ResourcePermFilterHandler - URI is in white list :{} skip resource permission check",requestUri);
                }else{
                    String token = request.getHeader(headerKey);
                    if(StringUtils.isBlank(token)){
                        log.info("AuthFilterHandler - token is blank");
                        ServiceException.throwError(SystemErrorCode.ERROR_410);
                    }
                    // 这一步会验证token，通过则获取用户信息，否则抛出异常被全局异常捕获返回结果
                    UserInfo userInfo = JwtUtil.getUserInfo(token);
                    if(userInfo != null){
                        // 查询用户关联的角色+权限代码列表，存到缓存中，登出时清空
                        // 1、菜单、tab页、按钮 级别的将权限作为一个接口，所有菜单和有权限的菜单提供给前端，从前端自己去渲染
                        // 2、接口级别的从后台校验
                        List<SysRoleResourceEntity> roleResByRoleId = sysRoleResourceService.findRoleResByRoleId(userInfo.getRoleId());
                        if(CollectionUtils.isNotEmpty(roleResByRoleId)){
                            CacheUtil.setObject(String.format(CACHE_PERMISSION_KEY,userInfo.getId()),CACHE_PERMISSION_SUB_KEY,roleResByRoleId);
                            checkRequestPermission(requestUri, roleResByRoleId);
                        }
                    }
                }
            }
        }
    }

    /**
     * 校验请求权限
     * @param requestURI
     * @param roleResByRoleId
     */
    private void checkRequestPermission(String requestURI, List<SysRoleResourceEntity> roleResByRoleId) {
        Set<String> resourceCodes = roleResByRoleId.stream().map(SysRoleResourceEntity::getResourceCode).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(resourceCodes)){
            List<SysResourceEntity> byCode = sysResourceService.findByCode(resourceCodes);
            if(CollectionUtils.isNotEmpty(byCode)){
                List<SysResourceEntity> resource = byCode.stream().filter(e -> e.getResourceType().equals("4") && e.getEnable() && e.getRouteUrl().equals(requestURI)).collect(Collectors.toList());
                if(!CollectionUtils.isNotEmpty(resource)){
                    log.info("AuthFilterHandler - token is blank");
                    ServiceException.throwError(SystemErrorCode.ERROR_401);
                }
            }
        }
    }
}
