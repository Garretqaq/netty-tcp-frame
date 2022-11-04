package com.datou.service;

import com.datou.protocol.pojo.MessageEntity;

/**
 * 用户校验
 * @author sgz
 * @since 1.0.0 2022/11/02
 */
public interface Checker {
    Boolean check(MessageEntity message);
}
