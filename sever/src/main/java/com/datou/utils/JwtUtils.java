package com.datou.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.datou.dto.UserDTO;
import com.datou.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUtils {
    /**
     * 盐
     */
    private final static String SALT = "123456";

    /**
     * 获取token
     * @param userId 用户id
     * @return token
     */
    public static String createToken(String userId, String md5Password){
        /*
          hutool的时间工具包
          now 定义为jwt的签发时间 生效时间
         */
        DateTime now = DateTime.now();
        DateTime expirationTime = now.offsetNew(DateField.HOUR, 200);//jwt的过期时间  当前时间+100;
        log.info("签发用户id：{}  |  签发时间：{}  |  过期时间：{}", userId, now, expirationTime);
        HashMap<String, Object> map = new HashMap<>();//JWTUtil.createToken(map,byte[])一个参数为map类型
        //签发时间
        map.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        map.put(JWTPayload.EXPIRES_AT, expirationTime);
        //生效时间
        map.put(JWTPayload.NOT_BEFORE, now);
        // 载荷 放了一个用户id ，角色权限  用户密码
        map.put("userId", userId);
        map.put("password", md5Password);
        map.put("role", 0);
        String md5 = SecureUtil.md5(SALT);//hutool工具包的MD5加密 也可以使用spring家的md5
        map.put("md5", md5);
        return JWTUtil.createToken(map, SALT.getBytes());
    }

    /**
     * 校验token
     * @param token token
     * @return 是否正确
     */
    public static Boolean checkToken(String token){
        return JWTUtil.verify(token, SALT.getBytes());
    }

    /**
     * 解析token
     * @param token token
     * @return 用户名-密码
     */
    public static UserDTO parseToken(String token){
        UserDTO user = new UserDTO();
        JWT jwt = JWTUtil.parseToken(token);
        String userId = jwt.getPayload("userId").toString();
        String password = jwt.getPayload("password").toString();

        user.setUserId(userId);
        user.setPassword(password);
        return user;
    }
}
