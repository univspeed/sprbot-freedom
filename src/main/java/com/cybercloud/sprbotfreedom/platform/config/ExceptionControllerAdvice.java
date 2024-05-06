package com.cybercloud.sprbotfreedom.platform.config;

import com.cybercloud.sprbotfreedom.platform.base.entity.Result;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.UnderLine2CamelUtil;

import com.mysql.cj.exceptions.DataTruncationException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 增强Controller，统一异常处理
 * @author liuyutang
 */
@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice{

    /**
     * 业务自定义异常拦截
     * @param exception
     * @return {{@link Result}}
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handleTokenFailure(ServiceException exception){
        Result result = null;
        log.error("{}",exception);
        if(exception!=null){
            result = Result.fail(exception.getCode(),exception.getMessage(),null);
        }
        return result;
    }

    /**
     * 业务自定义异常拦截
     * @param exception
     * @return {{@link Result}}
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result exception(Exception exception){
        Result result = null;
        log.error("{}",exception);
        if(exception!=null){
            result = Result.fail(SystemErrorCode.ERROR_500);
        }
        return result;
    }

    /**
     * 登录失效异常拦截
     * @param expiredJwtException
     * @return {{@link Result}}
     */
    @ExceptionHandler({ExpiredJwtException.class})
    @ResponseBody
    public Result handleTokenFailure( ExpiredJwtException expiredJwtException){
        log.error("{}",expiredJwtException);
        if( expiredJwtException!=null){
            return Result.fail(SystemErrorCode.ERROR_410);
        }
        return Result.fail();
    }


    /**
     * 请求方式错误异常拦截处理
     * @param exception
     * @return {{@link Result}}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result unSupportedRequestMethod(HttpRequestMethodNotSupportedException exception){
        log.error("{}",exception);
        if(exception!=null){
            return Result.fail(SystemErrorCode.ERROR_405);
        }
        return Result.fail();
    }

    /**
     * 请求方式错误异常拦截处理
     * @param exception
     * @return {{@link Result}}
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Result IllegalArgumentException(IllegalArgumentException exception){
        log.error("{}",exception);
        if(exception!=null){
            return Result.fail(SystemErrorCode.ERROR_40002.getCode(), exception.getMessage(),null);
        }
        return Result.fail();
    }

    /**
     * 此为validation异常拦截处理
     * @param e
     * @return {{@link Result}}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        log.error("{}",e);
        if(e!=null){
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            List<String> list = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(constraintViolations)){
                int i = 1;
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                    // 封装错误消息为字符串
                    String exMsg = constraintViolation.getMessage();;
                    if (i != 0 ){
                        sb.append(String.format("%s. %s", i, exMsg)).append("<br/>");
                    } else {
                        sb.append(exMsg).append(list.size() > 1 ? "<br/>" : "");
                    }
                    i++;
                }
                return Result.fail(SystemErrorCode.ERROR_424.getCode(),
                        String.format("%s[%s]",SystemErrorCode.ERROR_424.getMsg(),sb),null);
            }
        }
        return Result.fail();
    }

    /**
     * 此为validation异常拦截处理
     * @param e
     * @return {{@link Result}}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("{}",e);
        if(e!=null){
            List<String> list = new ArrayList<>();
            BindingResult bindingResult = e.getBindingResult();
            if(bindingResult != null){
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                if(CollectionUtils.isNotEmpty(allErrors)){
                    int i = 1;
                    StringBuilder sb = new StringBuilder();
                    for (ObjectError error : allErrors) {
                        // 封装错误消息为字符串
                        String exMsg = error.getDefaultMessage();
                        if (i != 0 ){
                            sb.append(String.format("%s. %s", i, exMsg)).append("<br/>");
                        } else {
                            sb.append(exMsg).append(list.size() > 1 ? "<br/>" : "");
                        }
                        i++;
                    }
                    return Result.fail(SystemErrorCode.ERROR_424.getCode(),
                            String.format("%s[%s]",SystemErrorCode.ERROR_424.getMsg(),sb),null);
                }
            }
        }
        return Result.fail();
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result handlerMissingServletRequestParameterException(MissingServletRequestParameterException parameterException){
        log.error("{}",parameterException);
        if(parameterException!=null){
            String message = parameterException.getMessage();
            if(StringUtils.isNotBlank(message)){
                message = String.format("参数名错误或缺少参数错误详情:%s",message);
            }else {
                message = SystemErrorCode.ERROR_400.getMsg();
            }
            return Result.fail(SystemErrorCode.ERROR_424.getCode(),
                    String.format("%s[%s]",SystemErrorCode.ERROR_424.getMsg(),message),null);
        }
        return Result.fail();
    }

    @ExceptionHandler(DataTruncationException.class)
    @ResponseBody
    public Result handlerMysqlDataTruncationException(DataTruncationException mysqlDataTruncation){
        log.error("{}",mysqlDataTruncation);
        if(mysqlDataTruncation!=null){
            String message = mysqlDataTruncation.getMessage();
            log.error("{}",message);
            if(StringUtils.isNotBlank(message)){
                String split = "'";
                if(message.contains(split)){
                    String column = UnderLine2CamelUtil.underline2Camel(
                            StringUtils.substring(message,message.indexOf(split)+1,message.lastIndexOf(split)).toLowerCase()
                    );
                    return Result.fail(SystemErrorCode.ERROR_424.getCode(),
                            String.format("%s[%s]",SystemErrorCode.ERROR_424.getMsg(),column),null);
                }
            }else {
                return Result.fail(SystemErrorCode.ERROR_424);
            }
        }
        return Result.fail();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result dataIntegerException(DataIntegrityViolationException exception){
        log.error("{}",exception);
        return Result.fail(SystemErrorCode.ERROR_999);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result dataIntegerException(SQLIntegrityConstraintViolationException exception){
        log.error("{}",exception);
        return Result.fail(SystemErrorCode.ERROR_999);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseBody
    public Result unexpectedTypeException(UnexpectedTypeException exception){
        log.error("{}",exception);
        return Result.fail(SystemErrorCode.ERROR_424.getCode(),
                exception.getMessage(),null);
    }


}
