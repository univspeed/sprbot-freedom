package com.cybercloud.sprbotfreedom.platform.util.queue;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liuyutang
 * @date 2023/10/30
 */
@Slf4j
@NoArgsConstructor
public class TaskWorkQueue<T>  {

    private ArrayBlockingQueue<T> queue = null;

    private UserWorkEventHandler<T> userWork;

    private final Object lockQueue = new Object();

    private boolean isOneThread = false;


    public TaskWorkQueue(int queueLength, boolean isWorkSequential, int priority) {
        isOneThread = isWorkSequential;
        queue = new ArrayBlockingQueue<>(20);
        Thread objQueueProcessThread = new Thread(this::processQueue);
        objQueueProcessThread.setPriority(priority);
        objQueueProcessThread.start();
    }

    public TaskWorkQueue(boolean isWorkSequential, int priority) {
        isOneThread = isWorkSequential;
        queue = new ArrayBlockingQueue<>(20);
        Thread objQueueProcessThread = new Thread(this::processQueue);
        objQueueProcessThread.setPriority(priority);
        objQueueProcessThread.start();
    }

    /**
     * 添加元素，如果队列已满则返回false 对应poll、peek
     * @param item
     * @return
     */
    public boolean offer(T item){
        return queue.offer(item);
    }

    /**
     * 方法从队列头部取出并移除元素，如果队列为空，将返回 null。对应 offer、peek
     * @return
     */
    public T poll(){
        return queue.poll();
    }

    public boolean isOneThread() {
        return isOneThread;
    }

    public void setOneThread(boolean oneThread) {
        isOneThread = oneThread;
    }

    public UserWorkEventHandler<T> getUserWork() {
        return userWork;
    }

    public void setUserWork(UserWorkEventHandler<T> userWork) {
        this.userWork = userWork;
    }

    /**
     * 绑定用户需要对队列中的item对象施加的操作的事件
      * @param <T>
     */
    @FunctionalInterface
    public interface UserWorkEventHandler<T> {
        void onUserWork(TaskWorkQueue<T> sender, EnqueueEventArgs<T> e);
    }

    // UserWork事件的参数，包含item对象
    public static class EnqueueEventArgs<T> {
        private T item;

        public EnqueueEventArgs(Object item) {
            try {
                this.item = (T) item;
            } catch (ClassCastException e) {
                throw new ClassCastException("failed to transfer object to T");
            }
        }
        public T getItem() {
            return this.item;
        }
    }

    // 处理队列中对象的函数
    private void processQueue() {
        while (true) {
            try {
                T item = null;
                boolean isEmpty = true;
                synchronized (lockQueue) {
                    if (!queue.isEmpty()) {
                        item = queue.poll();
                        isEmpty = false;
                    }
                }

                if (isEmpty) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                if (userWork != null && item != null) {
                    if (isOneThread) {
                        userWork.onUserWork(this, new EnqueueEventArgs<>(item));
                    } else {

                        // 使用线程池来执行任务
                        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(32);
                        T finalItem = item;
                        threadPool.execute(() -> userWork.onUserWork(this, new EnqueueEventArgs<>(finalItem)));
                        threadPool.shutdown();
                    }
                }
            } catch (Exception ex) {
                log.error("Exception occur when the workqueue callback, message:{}, StackTrace:{}", ex.getMessage(), ex.getStackTrace());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
