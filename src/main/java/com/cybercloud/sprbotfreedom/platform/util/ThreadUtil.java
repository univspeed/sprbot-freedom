package com.cybercloud.sprbotfreedom.platform.util;

import java.util.concurrent.*;

/**
 * 线程工具类
 * @author liuyutang
 * @date 2023/8/3
 */
public class ThreadUtil {

    /**
     * 核心线程数，即保持活动状态的线程数，即使它们处于空闲状态也不会被终止
     */
    private static int corePoolSize = 10;
    /**
     * 最大线程数，线程池允许创建的最大线程数
     */
    private static int maximumPoolSize = 20;
    /**
     * 线程空闲超时时间，超过核心线程数的线程在空闲超过此时间后会被终止
     */
    private static long keepAliveTime = 60;
    /**
     * 超时时间的单位
     */
    private static TimeUnit unit = TimeUnit.SECONDS;
    /**
     * 工作队列，用于存放待执行的任务
     */
    private static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
    /**
     * 线程工厂，用于创建新的线程
     */
    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    /**
     * 拒绝策略，用于处理任务队列满时的拒绝操作
     */
    private static RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workQueue,
            threadFactory,
            handler);

    public static ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(
            corePoolSize,
            threadFactory,
            handler
    );
}
