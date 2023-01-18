package com.datou.service.impl;

import cn.hutool.db.sql.SqlBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datou.dto.UserDTO;
import com.datou.entity.User;
import com.datou.entity.mapper.UserMapper;
import com.datou.protocol.pojo.MessageEntity;
import com.datou.protocol.pojo.UserMessage;
import com.datou.serializer.JsonSerializer;
import com.datou.service.Checker;
import com.datou.sources.MySqlSourcesFactory;
import com.datou.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.sql.Wrapper;
import java.util.Map;

/**
 * 用户校验
 * @author sgz
 * @since 1.0.0 2022/11/02
 */
public class CheckerImpl implements Checker {
    @Override
    public Boolean check(MessageEntity message) {
        String body = message.getBody();
        UserMessage userMessage = JsonSerializer.deserialize(body, UserMessage.class);

        // token校验
        boolean tokenStatus = false;
        String token = userMessage.getToken();
        if (StringUtils.isNotBlank(token)){
            UserDTO userDTO = JwtUtils.parseToken(token);
            String userId = userDTO.getUserId();
            String password = userDTO.getPassword();
            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(password)){
                LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(User::getUserId, userId)
                        .eq(User::getPassword, password);

                SqlSession sqlSession = MySqlSourcesFactory.getSqlSession();
                UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            }



        }
        return null;
    }

}
