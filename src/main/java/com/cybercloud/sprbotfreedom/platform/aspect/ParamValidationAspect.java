package com.cybercloud.sprbotfreedom.platform.aspect;

import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.platform.annotation.validation.ParamValidation;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.ValidLevel;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.validator.BeanValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 参数校验切面
 * @author liuyutang
 */
@Aspect
@Component
@Slf4j
public class ParamValidationAspect {

    @Autowired
    private BeanValidator beanValidator;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    @Qualifier("baseServiceImpl")
    private BaseService baseService;

    private static final String GROUP = "group";


    @Pointcut("@annotation(com.cybercloud.sprbotfreedom.platform.annotation.validation.ParamValidation)")
    public void paramValid() {}

    @Pointcut("execution(* com.cybercloud.sprbotfreedom.web.controller..*.*(..))")
    public void controller() {}

    @Before("paramValid() && controller()")
    public void paramValid(JoinPoint joinPoint){
        String group = httpServletRequest.getParameter(GROUP);
        if(StringUtils.isBlank(group)){
            group = (String)baseService.getBodyParam(GROUP);
        }
        if(StringUtils.isNotBlank(group)){
            log.info("校验规则组为：{}",group);
            MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            //获取参数的注解 1维是参数，2维是注解
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            if(ArrayUtils.isEmpty(parameterAnnotations)){
                for (Annotation[] parameterAnnotation : parameterAnnotations) {
                    //参数下标
                    if(ArrayUtils.isEmpty(parameterAnnotations)){
                        int indexOf = ArrayUtils.indexOf(parameterAnnotations, parameterAnnotation);
                        for (Annotation annotationParam : parameterAnnotation) {
                            //如果参数上标记了则直接使用参数的注解
                            if(annotationParam.annotationType().equals(ParamValidation.class)){
                                throwValidException(group,joinPoint.getArgs()[indexOf]);
                            }
                            //如果参数上没标记，则按方法上的注解
                            else {
                                methodValidtor(joinPoint.getArgs(),method,group);
                            }
                        }
                    }
                }
            }else {
                methodValidtor(joinPoint.getArgs() ,method,group);
            }
        }else {
            throw new ServiceException(SystemErrorCode.ERROR_423);
        }
    }

    /**
     * 参数校验
     * @param args 参数
     * @param method 注解
     * @param group 组
     */
    private void methodValidtor(Object[] args ,Method method,String group){
        //获取方法的注解
        ParamValidation annotation = method.getAnnotation(ParamValidation.class);
        if(annotation != null){
            ValidLevel validLevel = annotation.validLevel();
            if(ArrayUtils.isNotEmpty(args)){
                //校验全部参数
                if(ValidLevel.ALL == validLevel){
                    for (Object arg : args) {
                        throwValidException(group, arg);
                    }
                }
                //校验部分参数
                else if(ValidLevel.FEW == validLevel){
                    int[] ints = annotation.validIndex();
                    if(ArrayUtils.isNotEmpty(ints)){
                        for (int i = 0; i < args.length; i++) {
                            //如果包含此对象索引
                            if(ArrayUtils.contains(ints,i)){
                                throwValidException(group, args[i]);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 抛出参数校验异常
     * @param group 组名称
     * @param arg 参数
     */
    private void throwValidException(String group, Object arg) {
        Class[] validatorGroup = beanValidator.getGroup(group);
        if(ArrayUtils.isNotEmpty(validatorGroup)){
            String validator = beanValidator.validator(arg,validatorGroup);
            if (StringUtils.isNotBlank(validator)) {
                log.error(validator);
                throw new ServiceException(SystemErrorCode.ERROR_424);
            }
        }
    }

}
