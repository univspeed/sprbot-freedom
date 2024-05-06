package com.cybercloud.sprbotfreedom.platform.util.queue;

import lombok.NoArgsConstructor;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 工作队列
 * @author liuyutang
 * @date 2023/8/4
 */
@NoArgsConstructor
public class QueueUtil<T extends Object> {

    private ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<>(20);

    private static QueueUtil INSTANCE = new QueueUtil();

    public static<T> QueueUtil<T> getInstance(){
        return INSTANCE;
    }

    /**
     * 添加元素，如果队列已满则报错 对应 remove
     * @param item
     * @return
     * @param <T>
     */
    public <T> boolean add(T item){
        return getInstance().queue.add(item);
    }

    /**
     * 取出元素，如果队列为空则报错 对应add
     * @return
     * @param <T>
     */
    public <T> T remove(){
        return (T)getInstance().queue.remove();
    }



    /**
     * 添加元素，如果队列已满则返回false 对应poll、peek
     * @param item
     * @return
     * @param <T>
     */
    public <T> boolean offer(T item){
        return getInstance().queue.offer(item);
    }

    /**
     * 方法从队列头部取出并移除元素，如果队列为空，将返回 null。对应 offer、peek
     * @return
     * @param <T>
     */
    public <T> T poll(){
        return (T)getInstance().queue.poll();
    }

    /**
     * 查看值 对应 offer、poll
     * @return
     * @param <T>
     */
    public <T> T peek(){
        return (T)getInstance().queue.peek();
    }

    /**
     * 添加元素 阻塞（对应take）
     * @param item
     * @return
     * @param <T>
     */
    public <T> void put(T item) throws InterruptedException {
        getInstance().queue.put(item);
    }

    /**
     * 取出元素 阻塞（对应put）
     * @return
     * @param <T>
     * @throws InterruptedException
     */
    public <T> T take() throws InterruptedException {
        return (T)getInstance().queue.take();
    }

    public static void main(String[] args) {
        QueueUtil.getInstance().offer(new Integer(1));
        QueueUtil.getInstance().offer(new Integer(2));
        QueueUtil.getInstance().offer(new Integer(3));
        QueueUtil.getInstance().offer(new Integer(4));

        System.out.println(QueueUtil.getInstance().<Integer>poll());
        System.out.println(QueueUtil.getInstance().<Integer>poll());
        System.out.println(QueueUtil.getInstance().<Integer>poll());
        System.out.println(QueueUtil.getInstance().<Integer>poll());
    }
}
