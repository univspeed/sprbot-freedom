package com.cybercloud.sprbotfreedom.platform.aspect;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevel;
import com.cybercloud.sprbotfreedom.platform.function.IfFunction;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.PrintLevelRetention;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Optional;

/**
 * 日志打印切面定义
 * @author liuyutang
 */
@Aspect
@Slf4j
@Component
public class PrintFunctionLogAspect implements InitializingBean {

    @Value("${system.log.print-function-level:info}")
    private String printFunctionLogLevel;
    @Value("${system.log.print-function-enable:false}")
    private boolean printFunctionLogEnable;
    private static final String SYMBOL = "--------------------------------------------------------------------------------------";
    private static final String METHOD_PATH = "method path:\t%s";
    private static final String PARAMETERS = "parameters:\t%s";
    private static final String RESPONSE = "response:\t%s";
    /**
     * 日志打印函数初始化
     */
    private final IfFunction<PrintLevel, Object> ifFunction = new IfFunction<>(new HashMap<>(5));

    @Override
    public void afterPropertiesSet() {
        ifFunction.add(PrintLevel.TRACE, (params) -> {
            for (Object param : params) {
                log.trace("{}", param);
            }
            return null;
        });
        ifFunction.add(PrintLevel.DEBUG, (params) -> {
            for (Object param : params) {
                log.debug("{}", param);
            }
            return null;
        });
        ifFunction.add(PrintLevel.INFO, (params) -> {
            for (Object param : params) {
                log.info("{}", param);
            }
            return null;
        });
        ifFunction.add(PrintLevel.WARN, (params) -> {
            for (Object param : params) {
                log.warn("{}", param);
            }
            return null;
        });
        ifFunction.add(PrintLevel.ERROR, (params) -> {
            for (Object param : params) {
                log.error("{}", param);
            }
            return null;
        });
    }

    /************以下是配置通知类型，可以是多个************/
    @Before("annotationLog()")
    public void beforeAdvice() {

    }

    /**
     * 日志注解切入点切入点
     */
    @Pointcut("@within(com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog)")
    public void annotationLog() {
    }

    @AfterReturning(returning = "returnObj", pointcut = "annotationLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object returnObj) {
        //判断开启打印日志
        if (printFunctionLogEnable) {
            PrintFunctionLog printFunctionLog = null;
            //获取类上的注解
            Class declaringType = joinPoint.getSignature().getDeclaringType();
            if (declaringType != null) {
                Annotation annotation = declaringType.getAnnotation(PrintFunctionLog.class);
                if (annotation!= null && annotation.annotationType().equals(PrintFunctionLog.class)) {
                    printFunctionLog = (PrintFunctionLog) annotation;
                }
            }
            //如果方法上有注解，就使用方法上的，否则使用类上的
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            PrintFunctionLog annotation = method.getAnnotation(PrintFunctionLog.class);
            if (annotation == null) {
                annotation = printFunctionLog;
            }
            if (annotation != null) {
                //判断符合日志级别
                PrintLevel level = annotation.level();
                PrintLevelRetention retention = annotation.retention();
                boolean printLog = false;
                if (retention == PrintLevelRetention.ONLY) {
                    PrintLevel byName = PrintLevel.getByName(printFunctionLogLevel);
                    if (byName != null) {
                        //ONLY模式，仅可以打印相等级别的日志
                        printLog = byName.getHeight() == level.getHeight();
                    }
                } else if (retention == PrintLevelRetention.UP) {
                    PrintLevel byName = PrintLevel.getByName(printFunctionLogLevel);
                    if (byName != null) {
                        //UP模式，可以打印大于等于当前等级的日志
                        printLog = byName.getHeight() <= level.getHeight();
                    }
                } else if (retention == PrintLevelRetention.FREE) {
                    PrintLevel byName = PrintLevel.getByName(printFunctionLogLevel);
                    PrintLevel[] levels = annotation.levels();
                    if (byName != null && ArrayUtils.isNotEmpty(levels)) {
                        //FREE模式，可以打印包含在levels数组中的日志
                        printLog = ArrayUtils.contains(levels, byName);
                    }
                }
                if (printLog) {
                    //请求方法地址
                    String methodPth = String.format("%s::%s", method.getDeclaringClass().getName(), method.getName());
                    //请求参数
                    StringBuilder params = new StringBuilder();
                    Parameter[] parameters = method.getParameters();
                    if (ArrayUtils.isNotEmpty(parameters)) {
                        Object[] args = joinPoint.getArgs();
                        int index = 0;
                        for (Parameter parameter : parameters) {
                            params.append(String.format("%s::%s=%s",
                                    parameter.getType().getName(), parameter.getName(), Optional.ofNullable(args[index]).orElse("null")));
                            params.append("\t");
                            index++;
                        }
                    }
                    ifFunction.doIf(PrintLevel.getByName(printFunctionLogLevel),
                            SYMBOL,
                            String.format(METHOD_PATH, methodPth),
                            String.format(PARAMETERS, params),
                            String.format(RESPONSE, returnObj),
                            SYMBOL);
                }
            }
        }
    }

}