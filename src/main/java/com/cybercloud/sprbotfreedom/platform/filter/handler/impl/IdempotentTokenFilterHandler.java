package com.cybercloud.sprbotfreedom.platform.filter.handler.impl;

import com.cybercloud.sprbotfreedom.platform.base.service.IdempotentTokenService;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.filter.chain.FilterHandlerChain;
import com.cybercloud.sprbotfreedom.platform.filter.handler.FilterHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 幂等性校验处理器
 * @author liuyutang
 * @date 2023/7/10
 */
@Slf4j
@Component("idempotentTokenHandler")
public class IdempotentTokenFilterHandler implements FilterHandler {

    @Value("${system.idempotent-check.open-idemptoken-check:false}")
    private boolean openIdempTokenFilter;
    @Value("${system.idempotent-check.idemp-white-list:}")
    private String whiteListUrls;
    @Value("${system.idempotent-check.header:idempotent_token}")
    private String idempotentTokenHeader;
    private final FilterHandlerChain filterHandlerChain;
    private final IdempotentTokenService idempotentTokenService;

    public IdempotentTokenFilterHandler(FilterHandlerChain filterHandlerChain, IdempotentTokenService idempotentTokenService) {
        this.filterHandlerChain = filterHandlerChain;
        this.idempotentTokenService = idempotentTokenService;
    }

    @Override
    public void handle() throws IOException {
        if(openIdempTokenFilter){
            log.info(">>>>>>>>> 幂等令牌过滤器就绪");
            HttpServletRequest request = ((HttpServletRequest) filterHandlerChain.getRequest());
            String requestUri = request.getRequestURI();
            if(StringUtils.isNotBlank(requestUri)){
                if(white(whiteListUrls,requestUri)){
                    log.info("IdempotentTokenFilterHandler - URI is in white list :{} skip idempotent token check",requestUri);
                }else{
                    if(!idempotentTokenService.checkToken(request.getHeader(idempotentTokenHeader))){
                        ServiceException.throwError(SystemErrorCode.ERROR_10002);
                    };
                }
            }
        }
    }

}
