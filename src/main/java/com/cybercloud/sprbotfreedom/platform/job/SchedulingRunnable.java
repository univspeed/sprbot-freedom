package com.cybercloud.sprbotfreedom.platform.job;

import com.cybercloud.sprbotfreedom.platform.util.SpringContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 定时任务运行线程
 * @author liuyutang
 * @date 2023/8/2
 */
@Slf4j
@Data
public class SchedulingRunnable implements Runnable{

    private String beanName;
    private String methodName;
    private String params;
    private Object targetBean;
    private Method method;

    @Override
    public void run() {
        log.info(">>> execute scheduled job start - bean：{}，method：{}，params：{}", beanName, methodName, params);
        long startTime = System.currentTimeMillis();
        try {
            if (StringUtils.hasText(params)) {
                method.invoke(targetBean, params);
            } else {
                method.invoke(targetBean);
            }
        } catch (Exception ex) {
            log.error("execute scheduled job error - bean：{}，method：{}，params：{} ", beanName, methodName, params, ex);
        }
        long times = System.currentTimeMillis() - startTime;
        log.info("execute scheduled job end - bean：{}，method：{}，params：{}，time：{} ms", beanName, methodName, params, times);
    }

    private void init() {
        try {
            targetBean = SpringContextUtils.getBean(beanName);
            if (StringUtils.hasText(params)) {
                method = targetBean.getClass().getDeclaredMethod(methodName, String.class);
            } else {
                method = targetBean.getClass().getDeclaredMethod(methodName);
            }
            ReflectionUtils.makeAccessible(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public SchedulingRunnable(String beanName, String methodName) {
        this(beanName, methodName, null);
    }

    public SchedulingRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
        init();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){ return true;}
        if (o == null || getClass() != o.getClass()){ return false;}
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }

        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                params.equals(that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }

        return Objects.hash(beanName, methodName, params);
    }
}
