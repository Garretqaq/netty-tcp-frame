package com.datou.protocol.pojo;


import lombok.Getter;
import lombok.Setter;

/**
 * 用户消息载体
 * @author sgz
 * @since 1.0.0 2022/11/02
 */
@Getter
@Setter
public class UserMessage {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * token校验
     */
    private String token;
    /**
     * 发送的消息
     */
    private String message;
}
