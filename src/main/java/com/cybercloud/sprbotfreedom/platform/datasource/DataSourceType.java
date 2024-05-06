package com.cybercloud.sprbotfreedom.platform.datasource;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 获取数据源类型
 * @author liuyutang
 */
@Slf4j
public class DataSourceType {

    public enum DataBaseType{
        DB1,DB2,DB3;
    }

    private static final ThreadLocal<DataBaseType> TYPE = new ThreadLocal<DataBaseType>();

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();
    public static void setDataBaseType(DataBaseType dataBaseType){
        writeLock.lock();
        try {
            if(dataBaseType==null){
                throw new NullPointerException();
            }
            log.info(">>> change current datasouce：{}",dataBaseType);
            TYPE.set(dataBaseType);
        }finally {
            writeLock.unlock();
        }
    }

    public static DataBaseType getDataBaseType(){
        readLock.lock();
        try {
            DataBaseType dataBaseType = TYPE.get() == null ? DataBaseType.DB1 : TYPE.get();
            log.info(">>> change current datasouce：{}",dataBaseType);
            return dataBaseType;
        }finally {
            readLock.unlock();
        }
    }

    public static void clearDataBaseTypew(){
        writeLock.lock();
        try {
            TYPE.remove();
        }finally {
            writeLock.unlock();
        }
    }
}
