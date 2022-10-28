package com.datou.handle;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 核对用户密码拦截器
 * @author sgz
 * @since 1.0.0 2022/10/28
 */
public class CheckHandle extends ChannelInboundHandlerAdapter {
    /**
     * 用户名
     */
    private String userName;

}
