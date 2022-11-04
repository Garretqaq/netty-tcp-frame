package com.datou.context;

import com.datou.protocol.pojo.MessageEntity;
import lombok.Getter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 解码后的消息载体
 * @author sgz
 * @since 1.0.0 2022/11/02
 */
@Getter
public class MessageContext {
    private static final BlockingQueue<MessageEntity> queue;

    static {
        queue = new LinkedBlockingQueue<>(1024);
    }

    /**
     * 提交消息
     * @param message 消息
     * @return 是否成功
     */
    public static Boolean submitMessage(MessageEntity message){
        return queue.offer(message);
    }

    /**
     * 取出消息
     * @return message
     * @throws InterruptedException 线程中断
     */
    public static MessageEntity takeMessage() throws InterruptedException {
        return queue.take();
    }

    /**
     * 当前信息容量
     * @return 队列数量
     */
    public static Integer size(){
        return queue.size();
    }
}
