package com.datou.protocol;

/**
 * 传输层协议头.
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
 *  @since 1.0.0 2022/10/26
 */
public enum HeaderEnum {
    /** sign: 0x01 ~ 0x03 =========================================================================================== */
    REQUEST(0x01, "请求"),
    RESPONSE(0x02, "响应"),
    NOTICE(0x03, "通知"),

    /** type: 0x11 ~ 0x23 =========================================================================================== */
    LOGIN(0x11, "登录"),
    REGISTER(0x12, "注册"),
    LOGOUT(0x13, "登出"),
    RECONNECT(0x14, "重连"),
    PERSON_MESSAGE(0x15, "发送个人消息"),

    GROUP_MESSAGE(0x16, "创建讨论组"),
    CREATE_GROUP(0x17, "登出"),
    DISBAND_GROUP(0x18, "解散讨论组"),
    ADD_MEMBER(0x19, "讨论组添加成员"),
    REMOVE_MEMBER(0x1a, "讨论组移除成员"),

    ADD_FRIEND(0x1b, "添加好友"),
    REMOVE_FRIEND(0x1c, "删除好友"),
    ALL_FRIEND(0x1d, "查看已添加好友"),

    UPDATE_SELF_INFO(0x1e, "修改个人信息"),
    LOOK_SELF_INFO(0x1f, "查看个人信息"),
    LOOK_FRIEND_INFO(0x21, "查看好友信息"),
    LOOK_GROUP_INFO(0x22, "查看自己所在讨论组信息"),

    HEARTBEAT(0x23, "心跳"),

    /** status: 0x31 ~ 0x34 ========================================================================================= */
    SUCCESS(0x31, "请求成功"),
    REQUEST_ERROR(0x32, "请求失败"),

    SERVER_BUSY(0x33, "服务器繁忙"),
    SERVER_ERROR(0x34, "服务器错误"),

    ILLEGAL(0x35, "非法数据包");

    public final byte key;
    public final String value;

    HeaderEnum(int key, String value) {
        this.key = (byte) key;
        this.value = value;
    }
}
