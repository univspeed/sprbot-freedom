package com.cybercloud.sprbotfreedom.platform.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 实体拷贝工具类
 * @author liuyutang
 */
@Slf4j
public class BeanUtil {
    /**
     * 实体基类基础字段集合
     */
    private static final List<String> BASE_FIELD = Stream.of("id","createTime","createUser","updateTime","updateUser","state").collect(Collectors.toList());

    /**
     * 拷贝实体
     * @param s 源对象
     * @param tClass 目标对象类对象
     * @param <S>
     * @param <T>
     * @return 目标对象
     */
    public static <S,T> T convertObject(S s,Class<T> tClass){
        if(s==null){return null;}
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (InstantiationException e) {
            log.error("{}",e);
        } catch (IllegalAccessException e) {
            log.error("{}",e);
        }
        BeanCopier copier = BeanCopier.create(s.getClass(), tClass, false);
        copier.copy(s, t, null);
        return t;
    }

    /**
     * 拷贝实体集合
     * @param sList 源对象集合
     * @param tClass 目标对象类对象
     * @param <S>
     * @param <T>
     * @return 目标对象
     */
    public static <S,T> List<T> convertObjectList(List<S> sList, Class<T> tClass){
        if(CollectionUtils.isEmpty(sList)){return Collections.emptyList();}
        List<T> list = new ArrayList<>(sList.size());
        for (S s : sList) {
            list.add(convertObject(s,tClass));
        }
        return list;
    }

    /**
     * 复制实体属性
     * @param s 源对象
     * @param t 目标对象
     * @param ignoreNull 是否忽略空
     * @param ignoreBaseField 是否忽略实体基本属性
     * @param <S>
     * @param <T>
     */
    public static <S,T> T copyProperties(S s , T t, boolean ignoreNull,boolean ignoreBaseField){
        if(s!=null && t!=null){
            String sJson = JSONObject.toJSONString(s);
            String tJson = JSONObject.toJSONString(t);
            Map sMap = JSONObject.parseObject(sJson, Map.class);
            Map tMap = JSONObject.parseObject(tJson, Map.class);
            sMap.forEach((k,v)->{
                //忽略属性
                if(!(ignoreBaseField && BASE_FIELD.contains(k))){
                    boolean strVal = false;
                    if(v instanceof String){
                        strVal = true;
                    }
                    //允许为空
                    if(!ignoreNull){
                        tMap.put(k,v);
                    }
                    //不可为空
                    else{
                        //String 类型值
                        if(strVal){
                            if(StringUtils.isNotBlank((String)v)){
                                tMap.put(k,v);
                            }
                        }
                        //其他类型值
                        else{
                            if(v!=null){
                                tMap.put(k,v);
                            }
                        }
                    }
                }
            });
            return JSONObject.parseObject(JSONObject.toJSONString(tMap),(Class<T>)t.getClass());
        }
        return t;
    }


    /**
     * 复制实体属性到集合
     * @param s 源对象
     * @param t 目标对象
     * @param ignoreNull 是否忽略空
     * @param ignoreBaseField 是否忽略实体基本属性
     * @param <S>
     * @param <T>
     */
    public static <S,T> List<T> copyPropertiesToList(List<S> s , Class<T> t, boolean ignoreNull,boolean ignoreBaseField){
        List<T> list = null;
        if(CollectionUtils.isNotEmpty(s) && t!=null){
            list = new ArrayList<>(s.size());
            for (S s1 : s) {
                T tt = null;
                try {
                    tt = t.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                String sJson = JSONObject.toJSONString(s1);
                String tJson = JSONObject.toJSONString(tt);
                Map sMap = JSONObject.parseObject(sJson, Map.class);
                Map tMap = JSONObject.parseObject(tJson, Map.class);
                sMap.forEach((k,v)->{
                    //忽略属性
                    if(!(ignoreBaseField && BASE_FIELD.contains(k))){
                        boolean strVal = false;
                        if(v instanceof String){
                            strVal = true;
                        }
                        //允许为空
                        if(!ignoreNull){
                            tMap.put(k,v);
                        }
                        //不可为空
                        else{
                            //String 类型值
                            if(strVal){
                                if(StringUtils.isNotBlank((String)v)){
                                    tMap.put(k,v);
                                }
                            }
                            //其他类型值
                            else{
                                if(v!=null){
                                    tMap.put(k,v);
                                }
                            }
                        }
                    }
                });
                list.add(JSONObject.parseObject(JSONObject.toJSONString(tMap),t));
            }
        }
        return list;
    }
}
