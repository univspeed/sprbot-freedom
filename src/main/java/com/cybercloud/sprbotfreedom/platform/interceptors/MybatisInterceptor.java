package com.cybercloud.sprbotfreedom.platform.interceptors;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.Properties;

/**
 * Mybatis拦截器
 * @author liuyutang
 * @date 2023/5/31
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MybatisInterceptor implements Interceptor {

    private String databaseProductName;
    public MybatisInterceptor(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        // 将数据库类型设置到参数对象中
        if (parameter instanceof Map) {
            Map<String, Object> paramMap = (Map<String, Object>) parameter;
            paramMap.put("databaseType", databaseProductName);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 返回目标对象的动态代理
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
