package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.constants.login.ResourceConstants;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import com.cybercloud.sprbotfreedom.platform.util.JwtUtil;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.sys.resouce.SysResourceEntity;
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
 * 接口权限处理器
 * @author liuyutang
 * @date 2023/7/11
 */
@Slf4j
@Component("interfacePermHandler")
public class InterfacePermFilterHandler implements FilterHandler {

    @Value("${system.interface-permission.open-permission-check:false}")
    private boolean openInterfacePermFilter;
    @Value("${system.auth-check.open-auth-check:true}")
    private boolean openAuthFilter;
    @Value("${system.interface-permission.interface-white-list:}")
    private String whiteListUrls;
    private final SysRoleResourceService sysRoleResourceService;
    private final FilterHandlerChain filterHandlerChain;
    @Value("${system.token.header}")
    private String tokenHeader;

    public InterfacePermFilterHandler(FilterHandlerChain filterHandlerChain, SysRoleResourceService sysRoleResourceService) {
        this.filterHandlerChain = filterHandlerChain;
        this.sysRoleResourceService = sysRoleResourceService;
    }

    @Override
    public void handle() throws IOException {
        if (openInterfacePermFilter && openAuthFilter) {
            log.info(">>>>>>>>> 接口权限过滤器就绪");
            HttpServletRequest request = ((HttpServletRequest) filterHandlerChain.getRequest());
            String requestUri = request.getRequestURI();
            String token = request.getHeader(tokenHeader);
            if(StringUtils.isNotBlank(requestUri)){
                if(white(whiteListUrls,requestUri)){
                    log.info("InterfacePermFilterHandler - URI is in white list :{} skip interface permission check",requestUri);
                }else{
                    UserInfo userInfo = JwtUtil.getUserInfo(token);
                    List<SysResourceEntity> resByRoleId = sysRoleResourceService.findResByRoleId(userInfo.getRoleId(), ResourceConstants.INTERFACE,true);
                    if(CollectionUtils.isEmpty(resByRoleId)){
                        ServiceException.throwError(SystemErrorCode.ERROR_401);
                    }
                    Set<SysResourceEntity> collect = resByRoleId.stream().filter(e -> requestUri.equals(e.getRouteUrl())).collect(Collectors.toSet());
                    if(CollectionUtils.isEmpty(collect)){
                        ServiceException.throwError(SystemErrorCode.ERROR_401);
                    }
                }
            }
        }
    }
}
