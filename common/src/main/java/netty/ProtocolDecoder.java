package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import protocol.HeaderConstant;
import protocol.pojo.MessageEntity;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


/**
 * 解析协议
 *
 *                                       协议样式
 *  __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __
 * |           |           |           |           |              |                          |
 *       2           1           1           1            4               body
 * |__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __ __|__ __ __ __ __ __ __ __ __|
 * |           |           |           |           |              |                          |
 *     Magic        Sign        Type       Status     Body Length         Body Content
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
@Slf4j
public class ProtocolDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < HeaderConstant.HEADER_LENGTH) {
            // 数据包长度小于协议头长度
            log.info("数据包长度小于协议头长度");
            return;
        }
        in.markReaderIndex();
        byte[] magic = new byte[2];
        // 读取ByteBuf两个字节
        in.readBytes(magic);
        if (!Arrays.equals(magic, HeaderConstant.MAGIC)) {
            // Magic不一致，表明不是自己的数据
            log.info("Magic不一致");
            in.resetReaderIndex();
            return;
        }

        // 开始解码
        byte sign = in.readByte();
        byte type = in.readByte();
        byte status = in.readByte();

        // 确认消息体长度
        int bodyLength = in.readInt();
        if (in.readableBytes() != bodyLength) {
            // 消息体长度不一致
            log.info("消息体长度不一致");
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[bodyLength];
        in.readBytes(bytes);
        // 将已经读过的消息, 谁处理谁释放
        in.discardReadBytes();

        // 将消息存入list交给下个handle处理
        MessageEntity message = new MessageEntity();
        message.setSign(sign);
        message.setType(type);
        message.setStatus(status);
        message.setBody(new String(bytes, StandardCharsets.UTF_8));
        out.add(message);
    }
}