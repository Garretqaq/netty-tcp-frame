package com.datou.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户实体
 * @author sgz
 * @since 1.0.0 2022/10/26
 */
@TableName("user")
@Getter
@Setter
public class User {
    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private Integer phone;
}
