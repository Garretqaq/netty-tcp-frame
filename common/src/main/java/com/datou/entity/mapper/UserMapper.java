package com.datou.entity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datou.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * from User")
    User getUser(String userId, String password);

}
