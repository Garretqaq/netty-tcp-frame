package com.datou.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳检测拦截器
 * @author sgz
 * @since 1.0.0 2022/10/28
 */
@Slf4j
public class HeartbeatHandle extends ChannelInboundHandlerAdapter {

    // 丢失的心跳数
    private int count = 0;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent)obj;
//            if (event.state()== IdleState.READER_IDLE){
//                log.info("客户端读超时");
//            }else if (event.state()== IdleState.WRITER_IDLE){
//                log.info("客户端写超时");
//            }else if (event.state()==IdleState.ALL_IDLE){
//                log.info("客户端所有操作超时");
//            }
            // 心跳丢失
            count++;
            if (count > 4) {
                // 心跳丢失数达到5个，主动断开连接
                ctx.channel().close();
            }
        }else {
            super.userEventTriggered(ctx, obj);
        }
    }


}
