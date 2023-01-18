package com.datou.handle;

import com.datou.context.MessageContext;
import com.datou.protocol.HeaderEnum;
import com.datou.protocol.pojo.MessageEntity;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 接受用户数据拦截并存入队列当中
 * @author sgz
 * @since 1.0.0 2022/11/2
 */
@Slf4j
public class AcceptorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MessageEntity) {
            MessageEntity messageHolder = (MessageEntity) msg;
            // 指定Channel
            messageHolder.setChannel(ctx.channel());
            // 添加到任务队列
            boolean offer = MessageContext.submitMessage(messageHolder);
            log.info("当前消息数量" + MessageContext.size());
            if (!offer) {
                // 服务器繁忙
                log.warn("服务器繁忙，拒绝服务");
                // 繁忙响应
                response(ctx.channel(), messageHolder.getSign());
            }
        } else {
            throw new IllegalArgumentException("msg is not instance of MessageHolder");
        }
    }

    /**
     * 服务器繁忙响应
     *
     * @param channel channel
     * @param sign 标识
     */
    private void response(Channel channel, byte sign) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSign(HeaderEnum.RESPONSE.key);
        messageEntity.setType(sign);
        messageEntity.setStatus(HeaderEnum.SERVER_BUSY.key);
        messageEntity.setBody("");
        channel.writeAndFlush(messageEntity);
    }
}
