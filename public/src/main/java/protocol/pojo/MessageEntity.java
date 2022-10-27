package protocol.pojo;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageEntity {
    /**
     * 消息标识
     */
    private byte sign;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 状态码
     */
    private byte status;
    /**
     * Json消息体
     */
    private String body;
    /**
     * 接受消息使用的channel
      */
    private Channel channel;
}
