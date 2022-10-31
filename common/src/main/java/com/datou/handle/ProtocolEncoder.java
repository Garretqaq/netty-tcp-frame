package com.datou.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import com.datou.protocol.HeaderConstant;
import com.datou.protocol.pojo.MessageEntity;

import java.nio.charset.StandardCharsets;

/**
 * 编码Handler.
 *
 *                                       协议样式
 *  __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __
 * |           |           |           |           |              |                          |
 *       2           1           1           1            4               body
 * |__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __ __|__ __ __ __ __ __ __ __ __|
 * |           |           |           |           |              |                          |
 *   Magic(TO)      Sign        Type       Status     Body Length         Body Content
 * |__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __ __|__ __ __ __ __ __ __ __ __|
 *
 * 协议头9个字节定长
 *     Magic      // 类似魔数一样,传输唯一标识，防止不合法数据包影响服务器性能
 *     Sign       // 消息标志，请求／响应／通知，byte类型
 *     Type       // 消息类型，登录／发送消息,等逻辑操作，byte类型
 *     Status     // 响应状态，类似于http的状态码，成功／失败，byte类型
 *     BodyLength // 协议体长度，int类型
 *
 *  @author sgz
 *  @since 1.0.0 2022/10/16
 */
public class ProtocolEncoder extends MessageToByteEncoder<MessageEntity> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageEntity msg, ByteBuf out) throws Exception {
        String body = msg.getBody();
        if (body == null) {
            throw new RuntimeException("消息体为空");
        }

        // 编码
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        // 写入到buf中
        out.writeBytes(HeaderConstant.MAGIC)
                .writeByte(msg.getSign())
                .writeByte(msg.getType())
                .writeByte(msg.getStatus())
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }
}
