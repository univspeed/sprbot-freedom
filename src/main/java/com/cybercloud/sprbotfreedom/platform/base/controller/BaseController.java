package com.cybercloud.sprbotfreedom.platform.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.FileRelatedEnum;
import com.cybercloud.sprbotfreedom.platform.enums.SystemErrorCode;
import com.cybercloud.sprbotfreedom.platform.exception.ServiceException;
import com.cybercloud.sprbotfreedom.platform.util.GetUserInfoUtil;
import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.platform.constants.file.FileConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 公共方法控制器基类
 * @author liuyutang
 */
@Slf4j
public class BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    @Qualifier("baseServiceImpl")
    private BaseService baseService;
    @Value("${system.token.header}")
    private String tokenHeader;
    /**
     * 返回数据列表字段名称
     */
    protected static final String DATA_LIST = "list";
    protected static final String ID = "id";
    protected static final String DATA = "data";
    /**
     * 默认动态表单取值别名
     */
    private static final String DEFAULT_ALIAS = "t";
    /**
     * 某些请求是否成功属性
     */
    protected static final String REQUEST_SUCCESS = "success";

    /**
     * 默认页数
     */
    protected static final int DEFAULT_PAGE = 1;
    protected static final String DEFAULT_PAGE_NAME = "page";
    /**
     * 默认页大小
     */
    protected static final int DEFAULT_PAGE_SIZE = 20;
    protected static final String DEFAULT_PAGE_SIZE_NAME = "pageSize";

    /**
     * 更新上传附件1的信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     */
    protected Set<String> bindFile1(Long relatedId, FileRelatedEnum fileRelatedEnum) {

        return bindFile( relatedId, fileRelatedEnum, FileConstants.DEFAULT_UPLOAD_FILE_ID_1);
    }

    /**
     * 更新所有上传的附件的信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     */
    protected Set<String> bindAllFile(Long relatedId, FileRelatedEnum fileRelatedEnum) {

        return bindFile( relatedId, fileRelatedEnum, FileConstants.DEFAULT_UPLOAD_FILE_ID_1,FileConstants.DEFAULT_UPLOAD_FILE_ID_2,FileConstants.DEFAULT_UPLOAD_FILE_ID_3);
    }

    /**
     * 更新上传文件信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     */
    protected Set<String> bindFile(Long relatedId, FileRelatedEnum fileRelatedEnum, String... requestFileIdName) {

        return baseService.bindFile(relatedId,fileRelatedEnum,requestFileIdName);
    }

    /**
     * 更新上传文件信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     */
    protected Set<String> bindFile(Long relatedId, FileRelatedEnum fileRelatedEnum, List<String> fileIds) {

        return baseService.bindFile(relatedId,fileRelatedEnum,fileIds);
    }

    /**
     * 分页参数设置
     * @param params 原参数列表
     * @return
     */
    protected Map<String,Object> addPageInfo(Map<String,Object> params){
        if(params==null){
            params = new HashMap<>(16);
        }
        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");
        if(StringUtils.isNotEmpty(page)){
            params.put("page",page);
        }
        if(StringUtils.isNotEmpty(pageSize)){
            params.put("pageSize",pageSize);
        }
        return params;
    }



    /**
     * 获取request中的全部参数,可忽略部分参数
     * @param ignoreParams 忽略的参数
     * 例如：  SysOrgEntity sysOrgEntity = getEntityByParam(SysOrgEntity.class, "en.*");
     *         EnterpriseEntity enterpriseEntity = getEntityByParam(EnterpriseEntity.class, "so.*");
     * @return
     */
    public Map getParams(String... ignoreParams){
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap(16);
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            if(ArrayUtils.isNotEmpty(ignoreParams)){
                for (String ignoreParam : ignoreParams) {
                    if(ignoreParam.contains(".*")){
                        int i = StringUtils.indexOf(ignoreParam, ".*");
                        if(i!=-1){
                            ignoreParam = ignoreParam.substring(0,StringUtils.indexOf(ignoreParam,".*"));
                            if(!name.startsWith(ignoreParam)){
                                returnMap.put(name, value);
                            }
                        }
                    }else {
                        if(!ArrayUtils.contains(ignoreParams,name)){
                            returnMap.put(name, value);
                        }
                    }
                }
            }else {
                returnMap.put(name,value);
            }
        }
        return returnMap;
    }

    /**
     * 参数转对象
     * @param t
     * @param ignoreParams
     * @param <T>
     * @return
     */
    private static final String POINT = ".";
    protected <T> T getEntityByParam(Class<T> t,String... ignoreParams){
        if(t!=null){
            Map<String,Object> params = getParams(ignoreParams);
            if(MapUtils.isNotEmpty(params)){
                Map<String,Object> copyParams = new HashMap(16);
                params.forEach((k,v)->{
                    if(k.contains(POINT)){
                        k = k.substring(StringUtils.indexOf(k,POINT)+1,k.length());
                    }
                    copyParams.put(k,v);
                });
                return JSONObject.parseObject(JSONObject.toJSONString(copyParams),t);
            }
        }
        return null;
    }

    /**
     * 获取所有Request参数
     * @param ignoreParamNames 忽略参数名
     * @return
     */
    protected Map getOrginalParams(String... ignoreParamNames){
        Map resultMap = new HashMap(16);
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(MapUtils.isNotEmpty(parameterMap)){
            parameterMap.forEach((k,v)->{
                Object value = null;
                if(!ArrayUtils.contains(ignoreParamNames,k)){
                    if(ArrayUtils.isNotEmpty(v)){
                        if(v.length==1){
                            value = v[0];
                        }else {
                            value = v;
                        }
                        resultMap.put(k,value);
                    }
                }
            });
        }
        return resultMap;
    }
    /**
     * 获取布尔类型参数
     * @param name 参数名
     * @return
     */
    protected Boolean getPara2Bool(String name){
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return Boolean.valueOf(parameter);
        }
        return null;
    }

    /**
     * 获取布尔类型参数
     * @param name 参数名
     * @param defaultValue 默认值
     * @return
     */
    protected Boolean getPara2Bool(String name,boolean defaultValue){

        return Optional.ofNullable(getPara2Bool(name)).orElse(defaultValue);
    }

    /**
     * 获取Integer类型数据
     * @param name 参数名
     * @return
     */
    protected Integer getPara2Int(String name){
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return Integer.valueOf(parameter);
        }
        return null;
    }

    /**
     * 获取Integer类型参数
     * @param name 参数名
     * @param defaultValue 默认值
     * @return
     */
    protected Integer getPara2Int(String name,Integer defaultValue){

        return Optional.ofNullable(getPara2Int(name)).orElse(defaultValue);
    }

    /**
     * 获取参数
     * @param name
     * @return
     */
    protected String getPara(String name){

        return request.getParameter(name);
    }

    /**
     * 获取参数
     * @param name 参数名
     * @param defaultValue 默认值
     * @return
     */
    protected String getPara(String name,String defaultValue){

        return Optional.ofNullable(request.getParameter(name)).orElse(defaultValue);
    }

    /**
     * 获取Long类型参数
     * @param name 参数名
     * @return
     */
    protected Long getPara2Long(String name){
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return Long.valueOf(parameter);
        }
        return null;
    }

    /**
     * 获取Long类型参数
     * @param name 参数名
     * @param defaultValue 默认值
     * @return
     */
    protected Long getPara2Long(String name,Long defaultValue){

        return Optional.ofNullable(getPara2Long(name)).orElse(defaultValue);
    }

    /**
     * 获取逗号分隔的字符数组
     * @param name
     * @return
     */
    protected String[] getParaStrArray(String name){
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return parameter.split(",");
        }
        return null;
    }

    /**
     * 获取逗号分隔的字符数组
     * @param name
     * @return
     */
    protected Set<String> getPara2Set(String name){
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return Arrays.asList(parameter.split(",")).stream().collect(Collectors.toSet());
        }
        return null;
    }

    protected <T> Set<T> toSet(T ... values){
        if(ArrayUtils.isNotEmpty(values)){
            return Arrays.asList(values).stream().collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 获取逗号分隔的字符数组
     * @param name
     * @return
     */
    protected List<String> getPara2List(String name){
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return Arrays.asList(parameter.split(","));
        }
        return null;
    }

    /**
     * 获取JSON字符对应的对象
     * @param name 参数名
     * @param tClass 类
     * @param <T>
     * @return
     */
    protected <T> T getParamJson2Obj(String name,Class<T> tClass){
        if(tClass==null){
            return null;
        }
        String parameter = request.getParameter(name);
        if(StringUtils.isNotBlank(parameter)){
            return JSONObject.parseObject(parameter,tClass);
        }
        return null;
    }

    /**
     * 获取int转boolean类型参数
     * @param name 参数名
     * @return
     */
    protected Boolean getParaInt2Bool(String name){
        Integer para2Int = getPara2Int(name);
        if(para2Int == null){
            return null;
        }
        return para2Int == 1;
    }

    /**
     * 获取int转boolean类型参数
     * @param name 参数名
     * @param defaultValue 默认值
     * @return
     */
    protected Boolean getParaInt2Bool(String name,Boolean defaultValue){

        return Optional.ofNullable(getParaInt2Bool(name)).orElse(defaultValue);
    }


    /**
     * 获取布尔参数转int类型
     * @param name 参数名
     * @return
     */
    protected Integer getParaBool2Int(String name){
        Boolean para2Bool = getPara2Bool(name);
        if(para2Bool == null){
            return null;
        }
        return para2Bool ? 1 : 0;
    }

    /**
     * 获取布尔参数转int类型
     * @param name 参数名
     * @param defaultValue 默认值
     * @return
     */
    protected Integer getParaBool2Int(String name,Integer defaultValue){

        return Optional.ofNullable(getParaBool2Int(name)).orElse(defaultValue);
    }


    /**
     * 获取Header值
     * @param name
     * @return
     */
    protected String getHeader(String name){
        return request.getHeader(name);
    }

    /**
     * 获取当前登录的用户信息
     * @return
     */
    protected UserInfo getUserInfo(){

        return GetUserInfoUtil.getUserInfo(request);
    }

    /**
     * 抛出异常
     * @param systemErrorCode
     */
    protected void throwError(SystemErrorCode systemErrorCode){
        ServiceException.throwError(systemErrorCode);
    }
}
