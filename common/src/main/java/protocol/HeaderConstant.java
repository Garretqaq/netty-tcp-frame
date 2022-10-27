package protocol;

import cn.hutool.core.util.CharsetUtil;

import java.nio.charset.StandardCharsets;


/**
 * 协议常用常量
 * @author sgz
 * @since 1.0.0 2022/10/16
 */
public class HeaderConstant {
    /**
     * 协议长度
     */
    public static final int HEADER_LENGTH = 9;

    /**
     * 魔数
     */
    public static final byte[] MAGIC = "TO".getBytes(StandardCharsets.UTF_8);
}
