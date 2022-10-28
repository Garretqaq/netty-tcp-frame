package com.datou.context;

import com.datou.protocol.pojo.MessageEntity;
import com.datou.utils.ThreadUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 已连接用户维护池
 * @author sgz
 * @since 1.0.0 2022/10/28
 */
@Slf4j
public class ConnectContext {
    /**
     * 连接池,已连接的用户
     */
    private static final Map<String, Channel> connect;

    /**
     * 任务队列
     */
    private static final BlockingQueue<MessageEntity> taskQueue;
    /**
     * 标识此消费任务已关闭
     */
    public static AtomicBoolean shutdown = new AtomicBoolean(false);
    static {
        taskQueue = new LinkedBlockingQueue<>();
        connect = new ConcurrentHashMap<>();
    }

    public static void build(){
        // 由单独一个线程监测队列任务状况，并分发交由其他线程处理
        ThreadUtils.execute(() -> {
            while (!shutdown.get()){
                try {
                    MessageEntity message = taskQueue.take();
                    log.info("执行任务----, 队列剩余{}", taskQueue.size());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    /**
     * 连接用户
     * @param userName 用户名
     * @param channel channel
     */
    public static void addUser(String userName,Channel channel){
        connect.put(userName, channel);
        log.info("用户：{}已加入连接池", userName);
    }

    /**
     * 删除用户
     * @param userName 用户名
     */
    public static void removeUser(String userName){
        connect.remove(userName);
        log.info("用户：{}已被移除连接池", userName);
    }

    /**
     * 查找用户对应的channel
     * @param userName 用户名
     */
    public static void getUserChannel(String userName){
        connect.get(userName);
    }

    /**
     * 添加任务到队列中
     * @param message 消息
     */
    public static void addTaskQueue(MessageEntity message){
        taskQueue.add(message);
    }
}
