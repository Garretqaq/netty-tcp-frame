package com.datou.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * @author sgz
 * @since 1.0.0 2022/10/28
 */
@Slf4j
public class ThreadUtils {
    /**
     * 工作队列，用来存储等待执行的任务
     * ArrayBlockingQueue：基于数组的有界阻塞队列，此队列按FIFO原则对元素进行排序
     */
    private static final ArrayBlockingQueue<Runnable> ARRAY_BLOCKING_QUEUE = new ArrayBlockingQueue<>(500);
    /**
     * 核心线程数
     * 当已创建的线程数大于corePoolSize后，任务将被放入任务队列中
     */
    private static final int CORE_POOL_SIZE = 4;
    /**
     * 最大线程数，线程池允许创建的最大线程数
     * 当任务队列已经放满了，且已创建线程数小于maximumPoolSize时，则线程池会创建新的线程执行任务
     */
    private static final int MAXIMUM_POOL_SIZE = 8;
    /**
     * 空闲线程存活时间
     * 只有当已创建的线程数大于corePoolSize时，此参数才会起作用
     */
    private static final long KEEP_ALIVE_TIME = 30L;
    /**
     * 参数keepAliveTime的时间单位
     */
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    /**
     * 线程池执行器
     */
    private static ThreadPoolExecutor executor;
    /**
     * 拒绝策略 CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
     */
    private static final RejectedExecutionHandler REJECTED_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    /**
     * 提交任务
     * @param command 线程
     */
    public static void execute(Runnable command) {
        getCommonThreadPoolInfo();
        executor.execute(command);
    }

    /**
     * 提交任务
     * @param command 线程
     */
    public static Future<?> submit(Runnable command) {
        getCommonThreadPoolInfo();
        return executor.submit(command);
    }

    private static void getCommonThreadPoolInfo() {
        log.info("CommonThreadPoolInfo========>当前线程总数：{}，正在执行任务线程数：{}，已执行完成任务数：{}",
                executor.getPoolSize(), executor.getActiveCount(), executor.getCompletedTaskCount());
    }

    // 初始化线程池执行器
    static {
        ThreadFactory THREAD_FACTORY = new BasicThreadFactory.Builder()
                .namingPattern("server-thread-d%").build();
        if (null == executor) {
            executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                    MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE_TIME,
                    UNIT,
                    ARRAY_BLOCKING_QUEUE,
                    THREAD_FACTORY,
                    REJECTED_HANDLER);
            log.info("成功初始化线程池执行器========>");
        }
    }

}