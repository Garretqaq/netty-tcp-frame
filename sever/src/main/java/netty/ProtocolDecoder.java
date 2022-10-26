package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import protocol.HeaderConstant;

import java.util.Arrays;
import java.util.List;

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



        out.add(messageHolder);
    }
}