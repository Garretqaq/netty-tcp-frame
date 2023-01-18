package com.datou.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户传输对象
 * @author sgz
 * @since 2022/12/01
 */
@Setter
@Getter
public class UserDTO {
    private String userId;

    private String password;
}
