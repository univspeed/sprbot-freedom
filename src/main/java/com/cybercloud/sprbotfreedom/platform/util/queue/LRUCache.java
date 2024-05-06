package com.cybercloud.sprbotfreedom.platform.util.queue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU缓存
 * @author liuyutang
 * @date 2023/8/4
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    private static final LRUCache INSTANCE = new LRUCache(20);

    public LRUCache(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    public static <K, V> LRUCache<K, V> getInstance(){
        return INSTANCE;
    }
}
