package com.cybercloud.sprbotfreedom.platform.base.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.entity.UserInfo;
import com.cybercloud.sprbotfreedom.platform.enums.FileRelatedEnum;
import com.cybercloud.sprbotfreedom.platform.file.FileService;
import com.cybercloud.sprbotfreedom.platform.function.WapperBO;
import com.cybercloud.sprbotfreedom.platform.util.*;
import com.cybercloud.sprbotfreedom.platform.base.entity.BaseEntity;
import com.cybercloud.sprbotfreedom.platform.base.service.BaseService;
import com.cybercloud.sprbotfreedom.platform.enums.aspect.StateEnum;
import com.cybercloud.sprbotfreedom.platform.function.WapperEntity;
import com.cybercloud.sprbotfreedom.platform.util.genid.IdWorker;
import com.cybercloud.sprbotfreedom.platform.util.json.JsonObject;
import com.cybercloud.sprbotfreedom.platform.wrapper.BodyReaderHttpServletRequestWrapper;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.file.FileAccessoryEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 业务实现基类
 * @author liuyutang
 * @param <E>
 * @param <D>
 */
@Slf4j
@Service("baseServiceImpl")
public class BaseServiceImpl<E extends BaseEntity,D extends BaseMapper<E>> implements BaseService<E> {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FileService fileService;
    @Autowired
    protected D dao;
    @Autowired
    private IdWorker idWorker;
    @Value("${system.auth-check.open-auth-check:true}")
    private boolean openAuthFilter;

    private static final String STATE_NAME  = "state";
    private static final String ORDER_BY_DESC_NAME  = "create_time";
    private static final String ID_NAME  = "id";
    private static final String LINE = "----------------------------";
    private static final String BODY_PARAM_NAME = "name=\"%s\"";
    private static final String PARAM_NAME = "%s=";
    private static final String JSON_PARAM_NAME = "\"%s\":";
    private static final String DEFAULT_CHARSET = "UTF-8";

    @Override
    public UserInfo getUserInfo() {
        if(request == null){return null;}
        return GetUserInfoUtil.getUserInfo(request);
    }

    @Override
    public String getRequestClientIp(){
        String viaHeader = request.getHeader("HTTP_VIA");
        if (viaHeader == null) {
            return request.getRemoteAddr();
        } else {
            return request.getHeader("HTTP_X_FORWARDED_FOR");
        }
    }

    /**
     * 将实体内放入更新信息
     * @param e
     * @param wapperEntity
     * @return
     */
    protected E updateInfo(E e , WapperEntity<E>... wapperEntity) {
        if (e == null) {
            return null;
        }
        if(openAuthFilter){
            UserInfo userInfo = getUserInfo();
            if (userInfo != null) {
                e.setUpdateUser(userInfo.getUsername());
            }
        }
        e.setUpdateTime(DateUtil.getDateTimeNoFormat());
        if(ArrayUtils.isNotEmpty(wapperEntity)){
            e = wapperEntity[0].wapper(e);
        }
        return e;
    }

    /**de
     * 未登录更新
     * @param e
     * @param wapperEntity
     * @return
     */
    protected E updateInfoUnLogin(E e , WapperEntity<E>... wapperEntity) {
        if (e == null) {
            return null;
        }

        e.setUpdateTime(DateUtil.getDateTimeNoFormat());
        if(ArrayUtils.isNotEmpty(wapperEntity)){
            e = wapperEntity[0].wapper(e);
        }
        return e;
    }

    /**
     * 将实体内放入保存信息
     * @param e
     * @param wapperEntity
     * @return
     */
    protected E saveInfo(E e,boolean snow, WapperEntity<E>... wapperEntity) {
        e = saveInfoSingle(e,snow);
        if (e == null) {
            return null;
        }
        if(openAuthFilter){
            UserInfo userInfo = getUserInfo();
            if (userInfo != null) {
                e.setCreateUser(userInfo.getUsername());
                e.setUpdateUser(userInfo.getUsername());
            }
        }
        if(ArrayUtils.isNotEmpty(wapperEntity)){
            e = wapperEntity[0].wapper(e);
        }
        return e;
    }

    /**
     * 未登录保存信息
     * @param e
     * @param wapperEntity
     * @return
     */
    protected E saveInfoUnLogin(E e,boolean snow, WapperEntity<E>... wapperEntity){
        e = saveInfoSingle(e,snow);
        if (e == null) {
            return null;
        }
        if(ArrayUtils.isNotEmpty(wapperEntity)){
            e = wapperEntity[0].wapper(e);
        }
        return e;
    }
    /**
     * 将实体内放入保存信息
     * @param e
     * @return
     */
    protected E saveInfoSingle(E e,boolean snow) {
        if (e == null) {
            return null;
        }
        if(null == e.getId()){
            if(snow){
                e.setId(idWorker.nextId());
            }else{
                e.setId((idWorker.nextId() >>> 16) & 0xFFFFFF);
            }
        }
        if(StringUtils.isBlank(e.getCreateTime())){
            e.setCreateTime(DateUtil.getDateTimeNoFormat());
        }
        if(StringUtils.isBlank(e.getUpdateTime())){
            e.setUpdateTime(DateUtil.getDateTimeNoFormat());
        }
        e.setState(Integer.valueOf(StateEnum.NORMAL.getCode()));
        return e;
    }

    @Override
    public String getUrl(String dyMethodUrl, String alias, Map<String,Object> params){
        if(StringUtils.isNoneBlank(dyMethodUrl,alias)){
            String format = String.format("%s:%s", alias, dyMethodUrl);
            if(MapUtils.isNotEmpty(params)){
                Set<String> paramNames = params.keySet();
                if(CollectionUtils.isNotEmpty(paramNames)){
                    String collect = paramNames.stream().filter(paramName->
                         null!=params.get(paramName)
                    ).map(paramName ->
                         String.format("%s.%s=%s", alias, paramName,params.get(paramName))
                    ).collect(Collectors.joining("&"));
                    return String.format("%s?%s",format,collect);
                }
            }else{
                return format;
            }
        }
        return null;
    }

    /**
     * 获取带有有效状态的LambdaQueryWrapper
     * @return
     */
    protected QueryWrapper<E> normalStateLambdaQueryWrapper(){
        return new QueryWrapper<E>()
                .eq(STATE_NAME,StateEnum.NORMAL.getCode())
                .orderByDesc(ORDER_BY_DESC_NAME);
    }

    /**
     * 获取带有有效状态的LambdaQueryWrapper
     * @return
     */
    protected QueryWrapper<E> normalStateLambdaQueryWrapperUnSort(){
        return new QueryWrapper<E>()
                .eq(STATE_NAME,StateEnum.NORMAL.getCode());
    }
    /**
     * 获取带有无效状态的LambdaQueryWrapper
     * @return
     */
    protected QueryWrapper<E> invalidStateLambdaQueryWrapper(){
        return new QueryWrapper<E>()
                .eq(STATE_NAME,StateEnum.INVALID.getCode());
    }

    /**
     * JSONArray 转 Java List
     * @param objects
     * @return
     */
    protected <T> List<T> jsonArrayToJavaList(JSONArray objects, Class<T> tClass){
        if(objects!=null && objects.size()>0){
            return objects.toJavaList(tClass);
        }
        return getEmptyList();
    }

    /**
     * JSONObject 转 Java Object
     * @param object
     * @return
     */
    protected <T> T jsonObjectToJavaObject(JSONObject object, Class<T> tClass){
        if(object != null){
            return object.toJavaObject(tClass);
        }
        return null;
    }

    /**
     * 转换不同类型的Page对象
     * @param sPage 原Page对象
     * @param tClass 新Page对象泛型
     * @param <S>
     * @param <T>
     * @return
     */
    protected <S,T> Page<T> copyPage(Page<S> sPage, Class<T> tClass){
        Page<T> result = new Page<>();
        if(sPage!=null && tClass!=null){
            result.setRecords(BeanUtil.convertObjectList(sPage.getRecords(),tClass));
            result.setSize(sPage.getSize());
            result.setTotal(sPage.getTotal());
            result.setPages(sPage.getPages());
            result.setCurrent(sPage.getCurrent());
            result.setOrders(sPage.orders());
            return result;
        }
        return setEmptyList(result);
    }

    /**
     * 将分页数据包装成分页对象
     * @param records
     * @param page
     * @param pageSize
     * @param <T>
     * @return
     */
    protected <T> Page<T> setPage(List<T> records,int page,int pageSize,int total){
        Page<T> emptyPage = getEmptyPage(page, pageSize);
        emptyPage.setRecords(records);
        emptyPage.setTotal(total);
        return emptyPage;
    };

    /**
     * 默认逻辑删除方法
     * @param ids 需要删除的id集合
     * @return
     */
    @Override
    public Set<Long> defaultLogicDelete(Set<Long> ids){
        if(CollectionUtils.isNotEmpty(ids)){
            List<E> es = dao.selectList(normalStateLambdaQueryWrapper().in(ID_NAME,ids));
            if(CollectionUtils.isNotEmpty(es)){
                es = defaultUpdate(es, true);
                if(CollectionUtils.isNotEmpty(es)){
                    return es.stream().filter(e -> null != e.getId()).map(E::getId).collect(Collectors.toSet());
                }
            }
        }
        return null;
    }

    /**
     * 默认逻辑删除方法
     * @param ids 需要删除的id集合
     * @return
     */
    @Override
    public Set<Long> defaultLogicDeleteUnLogin(Set<Long> ids){
        if(CollectionUtils.isNotEmpty(ids)){
            List<E> es = dao.selectList(normalStateLambdaQueryWrapper().in(ID_NAME,ids));
            if(CollectionUtils.isNotEmpty(es)){
                es = defaultUpdateUnLogin(es, true);
                if(CollectionUtils.isNotEmpty(es)){
                    return es.stream().filter(e -> null != e.getId()).map(E::getId).collect(Collectors.toSet());
                }
            }
        }
        return null;
    }

    @Override
    public Set<Long> defaultLogicDelete(Long... ids) {
        if(ArrayUtils.isNotEmpty(ids)){
            return defaultLogicDelete(Arrays.asList(ids).stream().collect(Collectors.toSet()));
        }
        return null;
    }

    /**
     * 默认逻辑删除方法
     * @param es 需要删除的对象集合
     * @return
     */
    @Override
    public Set<Long> defaultLogicDelete(List<E> es){
        if(CollectionUtils.isNotEmpty(es)){
            if(CollectionUtils.isNotEmpty(es)){
                es = defaultUpdate(es, true);
                if(CollectionUtils.isNotEmpty(es)){
                    return es.stream().filter(e -> null != e.getId()).map(E::getId).collect(Collectors.toSet());
                }
            }
        }
        return null;
    }

    @Override
    public Set<Long> defaultLogicDeleteUnLogin(Long... ids) {
        if(ArrayUtils.isNotEmpty(ids)){
            return defaultLogicDeleteUnLogin(Arrays.asList(ids).stream().collect(Collectors.toSet()));
        }
        return null;
    }

    /**
     * 默认逻辑删除方法
     * @param es 需要删除的对象集合
     * @return
     */
    @Override
    public Set<Long> defaultLogicDeleteUnLogin(List<E> es){
        if(CollectionUtils.isNotEmpty(es)){
            if(CollectionUtils.isNotEmpty(es)){
                es = defaultUpdateUnLogin(es, true);
                if(CollectionUtils.isNotEmpty(es)){
                    return es.stream().filter(e -> null != e.getId()).map(E::getId).collect(Collectors.toSet());
                }
            }
        }
        return null;
    }

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    @Override
    public E defaultUpdate(E e){
        if(e != null && null != e.getId()){
           return defaultUpdate(e,false);
        }
        return null;
    }

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    @Override
    public E defaultUpdateUnLogin(E e, boolean delete){
        if(e != null && null != e.getId()){
            e = updateInfoUnLogin(e,(en)->{
                if(delete){
                    en.setState(StateEnum.INVALID.getCode());
                }
                return en;
            });
            int i = dao.updateById(e);
            if(i > 0){
                return e;
            }
        }
        return null;
    }

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    @Override
    public E defaultUpdateUnLogin(E e){
        if(e != null && null != e.getId()){
            return defaultUpdateUnLogin(e,false);
        }
        return null;
    }


    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    @Override
    public List<E> defaultUpdate(List<E> e){
        if(CollectionUtils.isNotEmpty(e)){
            return defaultUpdate(e,false);
        }
        return getEmptyList();
    }

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @return
     */
    @Override
    public List<E> defaultUpdateUnLogin(List<E> e){
        if(CollectionUtils.isNotEmpty(e)){
            return defaultUpdateUnLogin(e,false);
        }
        return getEmptyList();
    }

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @param delete 是否需要逻辑删除
     * @return
     */
    @Override
    public E defaultUpdate(E e, boolean delete){
        if(e != null && null != e.getId()){
            e = updateInfo(e,(en)->{
                if(delete){
                    en.setState(StateEnum.INVALID.getCode());
                }
                return en;
            });
            int i = dao.updateById(e);
            if(i > 0){
                return e;
            }
        }
        return null;
    }

    /**
     * 默认更新方法
     * @param e 需要更新的对象
     * @param delete 是否需要逻辑删除
     * @return
     */
    @Override
    public E defaultUpdateSingle(E e, boolean delete){
        if(e != null && null != e.getId()){
            if(delete){
                e.setState(StateEnum.INVALID.getCode());
            }
            int i = dao.updateById(e);
            if(i > 0){
                return e;
            }
        }
        return null;
    }

    /**
     * 默认更新方法
     * @param es 需要更新的对象集合
     * @param delete 是否需要逻辑删除
     * @return
     */
    private List<E> defaultUpdate(List<E> es,boolean delete){
        if(CollectionUtils.isNotEmpty(es)){
            List<E> result = new ArrayList<>(es.size());
            if(CollectionUtils.isNotEmpty(es)){
                for (E e : es) {
                    e = defaultUpdate(e, delete);
                    if(e != null){
                        result.add(e);
                    }
                }
                return result;
            }
        }
        return getEmptyList();
    }

    /**
     * 默认更新方法
     * @param es 需要更新的对象集合
     * @param delete 是否需要逻辑删除
     * @return
     */
    private List<E> defaultUpdateUnLogin(List<E> es,boolean delete){
        if(CollectionUtils.isNotEmpty(es)){
            List<E> result = new ArrayList<>(es.size());
            if(CollectionUtils.isNotEmpty(es)){
                for (E e : es) {
                    e = defaultUpdateUnLogin(e, delete);
                    if(e != null){
                        result.add(e);
                    }
                }
                return result;
            }
        }
        return getEmptyList();
    }

    /**
     * 默认保存方法
     * @param e
     * @return
     */
    @Override
    public E defaultSave(E e,boolean snow){
        if(e != null){
            e = saveInfo(e,snow);
            int insert = dao.insert(e);
            if(insert > 0){
                return e;
            }
        }
        return null;
    }

    /**
     * 默认保存方法
     * @param e
     * @return
     */
    @Override
    public E defaultSaveUnLogin(E e,boolean snow){
        if(e != null){
            e = saveInfoUnLogin(e,snow);
            int insert = dao.insert(e);
            if(insert > 0){
                return e;
            }
        }
        return null;
    }

    @Override
    public List<E> defaultSave(List<E> es,boolean snow) {
        List<E> result = getEmptyList();
        if(CollectionUtils.isNotEmpty(es)){
            result = new ArrayList<>(es.size());
            for (E e : es) {
                E save = defaultSave(e,snow);
                if(save != null){
                    result.add(save);
                }
            }
        }
        return result;
    }

    @Override
    public List<E> defaultSaveUnLogin(List<E> es,boolean snow) {
        List<E> result = getEmptyList();
        if(CollectionUtils.isNotEmpty(es)){
            result = new ArrayList<>(es.size());
            for (E e : es) {
                E save = defaultSaveUnLogin(e,snow);
                if(save != null){
                    result.add(save);
                }
            }
        }
        return result;
    }
    @Override
    public Object getBodyParam(String paramName){
        if(StringUtils.isNotBlank(paramName)){
            BufferedReader br;
            try {
                BodyReaderHttpServletRequestWrapper wrapper = new BodyReaderHttpServletRequestWrapper(request);
                br = wrapper.getReader();
                StringBuffer sbf = new StringBuffer();
                String str;
                while ((str = br.readLine()) != null){
                    sbf.append(str);
                }
                String string = sbf.toString();
                //避免%导致decode转码错误
                string = string.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                String body = URLDecoder.decode(string,DEFAULT_CHARSET);
                if(StringUtils.isNotBlank(body)){
                    if(JsonObject.isJson(body)){
                        Map<String,Object> bodyMap = JSONObject.parseObject(body, Map.class);
                        return findDeepMap(bodyMap, paramName);
                    }
                    String format = String.format(BODY_PARAM_NAME, paramName);
                    String paramNameFormat = String.format(PARAM_NAME, paramName);
                    int paramIndex = body.indexOf(format);
                    int paramNameIndex = body.indexOf(paramNameFormat);
                    if(paramIndex != -1){
                        body = body.substring(paramIndex+format.length(),body.length());
                        body = body.substring(0,body.indexOf(LINE));
                        return body;
                    }
                    if(paramNameIndex!=-1){
                        String split = "&";
                        int i = body.lastIndexOf(split);
                        if(i < paramNameIndex){
                            i = body.length();
                        }
                        return body.substring(paramNameIndex+paramNameFormat.length(),i);
                    }
                }
            }catch (IOException e){
                log.error("{}",e);
            }
        }
        return null;
    }

    @Override
    public E defaultFindOne(Long id) {
        if(null != id){
            return dao.selectOne(normalStateLambdaQueryWrapper().eq(ID_NAME,id));
        }
        return null;
    }

    @Override
    public <T> T defaultFindOne(Long id,Class<T> convert) {
        if(null != id){
            return BeanUtil.convertObject(defaultFindOne(id),convert);
        }
        return null;
    }


    @Override
    public Map<Long,E> defaultFindByIds(Set<Long> ids) {
        if(CollectionUtils.isNotEmpty(ids)){
            List<E> es = dao.selectList(normalStateLambdaQueryWrapper().in(ID_NAME,ids));
            if(CollectionUtils.isNotEmpty(es)){
                return es.stream().collect(Collectors.toMap(E::getId,e -> e,(k1,k2)->k2));
            }
        }
        return null;
    }

    @Override
    public Map<Long,E> defaultFindByIds(Long... ids) {
        if(ArrayUtils.isNotEmpty(ids)){
            List<E> es = dao.selectList(normalStateLambdaQueryWrapper().in(ID_NAME,ids));
            if(CollectionUtils.isNotEmpty(es)){
                return es.stream().collect(Collectors.toMap(E::getId,e -> e,(k1,k2)->k2));
            }
        }
        return null;
    }

    @Override
    public List<E> defaultFindAll() {

        return setEmptyList(dao.selectList(normalStateLambdaQueryWrapper()));
    }

    @Override
    public List<FileAccessoryEntity> getFilesByParam(FileRelatedEnum fileRelatedEnum, String relatedField, Long relatedId){
        String s = JSONObject.toJSONString(fileService.findByRelatedParam(fileRelatedEnum.getRelatedType(), fileRelatedEnum.getRelatedTable(), relatedField, relatedId));
        return jsonArrayToJavaList(JSONArray.parseArray(s), FileAccessoryEntity.class);
    }

    /**
     * 更新上传文件信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     */
    @Override
    public Set<Long> bindFile(Long relatedId, FileRelatedEnum fileRelatedEnum, String... requestFileIdName) {
        if(null != relatedId && fileRelatedEnum!=null) {
            List<Long> fileIds = getFileIds(requestFileIdName);
            log.info("绑定数据ID:{}，绑定参数:fileRelatedEnum[RelatedTable:{},relatedType:{}],绑定文件ID列表:[{}]",
                    relatedId,fileRelatedEnum.getRelatedTable(),fileRelatedEnum.getRelatedType(),fileIds);
            return fileService.bindFileAndDataId(fileIds.stream().collect(Collectors.toSet()),fileRelatedEnum,relatedId);
        }
        return null;
    }

    /**
     * 更新上传文件信息
     * @param relatedId 关系id
     * @param fileRelatedEnum 关系枚举
     */
    @Override
    public Set<Long> bindFile(Long relatedId, FileRelatedEnum fileRelatedEnum, List<Long> fileIds) {
        if(null != relatedId && fileRelatedEnum!=null) {
            if(CollectionUtils.isEmpty(fileIds)){
                fileIds = Collections.EMPTY_LIST;
            }
            log.info("绑定数据ID:{}，绑定参数:fileRelatedEnum[RelatedTable:{},relatedType:{}],绑定文件ID列表:[{}]",
                    relatedId,fileRelatedEnum.getRelatedTable(),fileRelatedEnum.getRelatedType(),fileIds);
            return fileService.bindFileAndDataId(fileIds.stream().collect(Collectors.toSet()),fileRelatedEnum,relatedId);
        }
        return null;
    }

    /**
     * 获取默认的文件ID
     * @param requestFileIdName
     * @return
     */
    @Override
    public List<Long> getFileIds(String... requestFileIdName){
        List<Long> filIds = new ArrayList<>();
        if(ArrayUtils.isNotEmpty(requestFileIdName)){
            for (String fileIdName : requestFileIdName) {
                String fileIds = request.getParameter(fileIdName);
                String[] paraStrArray = Optional.ofNullable(fileIds).map(s -> s.split(",")).orElse(null);
                if(paraStrArray==null){
                    Object bodyParam = getBodyParam(fileIdName);
                    if(bodyParam!=null){
                        if(bodyParam instanceof JSONArray){
                            JSONArray jsonArray = (JSONArray)bodyParam;
                            for(int i =0 ; i<jsonArray.size();i++){
                                if(jsonArray.get(i) instanceof JSONArray){
                                    List<Long> strings = JSONArray.parseArray(JSONObject.toJSONString(jsonArray.get(i)), Long.class);
                                    if(CollectionUtils.isNotEmpty(strings)){
                                        filIds.addAll(strings);
                                    }
                                }else {
                                    filIds.add((Long)jsonArray.get(i));
                                }
                            }
                        }else{
                            String ids = (String)bodyParam;
                            Arrays.asList(ids.split(",")).stream().forEach(id->{
                                filIds.add(Long.valueOf(id));
                            });
                        }
                    }
                }else{
                    for (String id : paraStrArray) {
                        filIds.add(Long.valueOf(id));
                    }
                }
            }
        }
        return filIds;
    }

    /**
     * 获取mybatis分页参数对象
     * @param page
     * @param pageSize
     * @return
     */
    protected RowBounds getRowBounds(int page,int pageSize){
        if(page < 0){page=1;}
        return new RowBounds((page-1) * pageSize , pageSize);
    }

    /**
     * 获取空的mybatis分页参数对象（传空则代表不分页）
     * @return
     */
    protected RowBounds getNullRowBounds(){
        return new RowBounds();
    }
    /**
     * Set 转 List
     * @param set
     * @param <T>
     * @return
     */
    protected <T> List<T> setToList(Set<T> set){
        if(CollectionUtils.isNotEmpty(set)){
            return set.stream().collect(Collectors.toList());
        }
        return getEmptyList();
    }

    /**
     * List 转 Set
     * @param list
     * @param <T>
     * @return
     */
    protected <T> Set<T> listToSet(List<T> list){
        if(CollectionUtils.isNotEmpty(list)){
            return list.stream().collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Object[] 转 Set
     * @param array
     * @param <T>
     * @return
     */
    protected <T> Set<T> objectArrayToSet(T [] array){
        if(ArrayUtils.isNotEmpty(array)){
            return Arrays.asList(array).stream().collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Object[] 转 List
     * @param array
     * @param <T>
     * @return
     */
    protected <T> List<T> objectArrayToList(T [] array){
        if(ArrayUtils.isNotEmpty(array)){
            return Arrays.asList(array).stream().collect(Collectors.toList());
        }
        return getEmptyList();
    }
    /**
     * 设置分页空数据
     * @param page
     * @param <T>
     */
    protected <T> Page<T> setEmptyList(Page<T> page){
        if(page!=null){
            List<T> records = page.getRecords();
            if(CollectionUtils.isEmpty(records)){
                page.setRecords(Collections.emptyList());
            }
            return page;
        }
        return new Page<>();
    }

    /**
     * 设置空列表数据
     * @param list
     * @param <T>
     * @return
     */
    protected <T> List<T> setEmptyList(List<T> list){
        if(list == null){
            return getEmptyList();
        }
        return list;
    }

    /**
     * 获取空列表数据
     * @return
     */
    protected List getEmptyList(){

        return Collections.emptyList();
    }

    /**
     * 获取空列表数据
     * @return
     */
    protected Set getEmptySet(){

        return Collections.emptySet();
    }

    /**
     * 获取空列表数据
     * @return
     */
    protected <T> Set<T> getEmptySet(Set<T> set){
        if(set == null){
            return getEmptySet();
        }
        return set;
    }

    /**
     * 获取空分页数据
     * @return
     */
    protected <T> Page<T> getEmptyPage(int page,int pageSize){

        return new Page<T>(page,pageSize);
    }

    /**
     * 获取空的分页数据
     * @param page
     * @param pageSize
     * @return
     */
    protected PageResult getEmptyPageUtils(int page, int pageSize){
        return PageResult.create(Collections.emptyList(),0,pageSize,page);
    }

    /**
     * 创建假分页
     * @param data
     * @param sortColumn
     * @param page
     * @param pageSize
     * @param wapper
     * @return
     * @param <T>
     * @param <R>
     */
    protected <T,R> PageResult<T> createPageResult(List<T> data, Function<T, R> sortColumn,boolean asc, int page, int pageSize, WapperBO<List<T>>...wapper){
        if(CollectionUtils.isNotEmpty(data)){
            //计算分页数据
            int totalCount = data.size();
            int totalPage = 0,maxPageCount = 0;
            if (totalCount % pageSize == 0) {
                totalPage = totalCount / pageSize;
                totalPage = totalPage == 0 ? 1 : totalPage;
            }
            else {
                totalPage = totalCount / pageSize + 1;
            }
            if (page <= 0) {
                page = 1;
            }
            if (page >= totalPage) {
                page = totalPage;
            }
            maxPageCount = totalPage;
            List<T> collect = data.stream().skip((page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
            Page<T> emptyPage = getEmptyPage(page, pageSize);
            emptyPage.setPages(maxPageCount);
            emptyPage.setTotal(totalCount);
            if(wapper != null){
                wapper[0].wapper(collect);
            }
            if(sortColumn != null){
                collect = collect.stream().sorted((o1,o2)->{
                    R a1 = sortColumn.apply(o1);
                    R a2 = sortColumn.apply(o2);
                    if(a1 instanceof Integer){
                        Integer v1 = (Integer) a1;
                        Integer v2 = (Integer) a2;
                        if(asc){
                            return v1 - v2;
                        }else{
                            return v2 - v1;
                        }
                    }
                    return 0;
                }).collect(Collectors.toList());
            }
            emptyPage.setRecords(collect);
            return PageResult.create(emptyPage);
        }
        return new PageResult(data,0,pageSize,page);
    }

    /**
     * 递归
     * @param map
     * @param paramName
     * @return
     */
    private Object findDeepMap(Map<String,Object> map,String paramName){
        Object result = null;
        if(MapUtils.isNotEmpty(map) && StringUtils.isNotBlank(paramName)){
            Object o = map.get(paramName);
            if(o!=null){
                result = o;
                return result;
            }
            for(Map.Entry<String,Object> entry : map.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                if(key.equals(paramName)){
                    result = value;
                    if(result!=null){
                        break;
                    }
                }else if(JsonObject.isJson(value)){
                    if(value instanceof JSONObject){
                        Map map1 = JSONObject.parseObject(JSONObject.toJSONString(value), Map.class);
                        result = findDeepMap( map1,paramName);
                        if(result!=null){
                            break;
                        }
                    }else if(value instanceof JSONArray){
                        JSONArray jsonArray = (JSONArray) value;
                        for(int i = 0; i < ((JSONArray) value).size() ; i++){
                            Object val = jsonArray.get(i);
                            if(val instanceof JSONObject){
                                Map map1 = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i)), Map.class);
                                result = findDeepMap(map1,paramName);
                                if(result!=null) {
                                    break;
                                }
                            }else{
                                result = val;
                                if(result!=null) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
