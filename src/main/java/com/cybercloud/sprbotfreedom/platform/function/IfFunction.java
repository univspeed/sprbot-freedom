package com.cybercloud.sprbotfreedom.platform.function;

import java.util.Map;

/**
 * 函数式接口定义
 * @author liuyutang
 */
public class IfFunction<K,T> {
    /**
     * 存放函数接口实现
     * K : 自定义函数式接口唯一标识
     * V : 函数式接口
     */
    private Map<K, Function> functionMap;

    public IfFunction(Map<K, Function> functionMap) {
        this.functionMap = functionMap;
    }

    /**
     * 添加函数到函数集合中
     * @param key 函数唯一标识
     * @param function 函数式接口
     * @return
     */
    public IfFunction<K,T> add(K key,Function<T> function){
        this.functionMap.put(key,function);
        return this;
    }

    /**
     * 执行集合中的指定函数
     * @param k 函数唯一标识
     * @param params 函数需要的参数
     * @return
     */
    public T doIf(K k,Object ... params){
        if(this.functionMap.containsKey(k)){
            return (T)functionMap.get(k).invoke(params);
        }
        return null;
    }

    /**
     * 执行集合中的指定函数，若此函数不在则执行默认函数
     * @param k 函数唯一标识
     * @param params 函数需要的参数
     * @return
     */
    public T doIfWithDefault(K k , Function<T> defaultFunction,Object ... params){
        if(this.functionMap.containsKey(k)){
            return (T)functionMap.get(k).invoke(params);
        }else {
            return defaultFunction.invoke(params);
        }
    }
}
