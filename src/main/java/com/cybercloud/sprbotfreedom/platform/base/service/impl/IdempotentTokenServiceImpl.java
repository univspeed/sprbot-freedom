package com.cybercloud.sprbotfreedom.platform.base.service.impl;


import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.IdempotentTokenEntity;
import com.cybercloud.sprbotfreedom.platform.base.service.IdempotentTokenService;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.CacheUtil;
import com.cybercloud.sprbotfreedom.platform.util.encrypt.AESUtil;
import com.cybercloud.sprbotfreedom.platform.util.encrypt.Md5Util;
import com.cybercloud.sprbotfreedom.platform.util.genid.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 接口幂等性token服务接口
 * @author liuyutang
 */
@Slf4j
@Service
@PrintFunctionLog
public class IdempotentTokenServiceImpl implements IdempotentTokenService {

    @Value("${system.idempotent-check.timeout: 5000}")
    private long tokenTimeOut;
    @Value("${system.idempotent-check.header:idempotent_token}")
    private String idempotentTokenHeader;
    @Value("${system.idempotent-check.token-aes-secret:cybercloud_identoken}")
    private String tokenAesSecret;
    private final Lock lock = new ReentrantLock();
    @Override
    public IdempotentTokenEntity getToken() {
        lock.lock();
        try {
            String uuid = UUIDUtil.genUUID();
            StringBuilder token = new StringBuilder();
            token.append(idempotentTokenHeader);
            token.append(uuid);
            String finalToken = token.toString();
            finalToken = AESUtil.encrypt(finalToken,tokenAesSecret);
            //设置token，超时时间设置影响两次方法t之间token验证失败的情况下调用的间隔
            String key = Md5Util.md5(finalToken);
            CacheUtil.setObject(idempotentTokenHeader,key,finalToken,tokenTimeOut);
            if(StringUtils.isNotBlank(finalToken)){
                return IdempotentTokenEntity.builder()
                        .key(key)
                        .token(finalToken)
                        .build();
            }
        }catch (Exception e){
            ServiceException.throwError(SystemErrorCode.ERROR_10000);
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public boolean checkToken(String token) {
        //未取到token参数
        if(StringUtils.isBlank(token)){
            ServiceException.throwError(SystemErrorCode.ERROR_10001);
        }
        lock.lock();
        try {
            String[] split = token.split("\\|");
            String key = split[0];
            token = split[1];
            // 校验完整性
            String aesToken = AESUtil.encrypt(token, tokenAesSecret);
            String md5Key = Md5Util.md5(aesToken);
            if(!md5Key.equals(key)){
                ServiceException.throwError(SystemErrorCode.ERROR_10002);
            }
            //如果redis中不存在此token
            String cacheToken = (String)CacheUtil.getObject(idempotentTokenHeader,key);
            if(StringUtils.isBlank(cacheToken)){
                ServiceException.throwError(SystemErrorCode.ERROR_10002);
            }
            if(cacheToken.equals(aesToken)){
                //如果token存在 ，删除redis中此token，然后放行此接口
                //如果删除失败，则再次抛出重复操作异常
                return true;
            }
            ServiceException.throwError(SystemErrorCode.ERROR_10002);
        } catch (Exception e) {
            log.error("{}",e);
            ServiceException.throwError(SystemErrorCode.ERROR_10002);
        }finally {
            lock.unlock();
        }
        return false;
    }
}
