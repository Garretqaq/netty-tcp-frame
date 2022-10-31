package com.datou.serializer;

import com.alibaba.fastjson.JSONObject;

/**
 * 使用fastJson序列化对象,方便解读
 * 若传输数据包多可采用Protobuf序列
 * @author sgz
 * @since 1.0.0 2022/10/26
 */
public class JsonSerializer {
    /**
     * 序列化
     *
     * @param object 对象
     * @return json
     */
    public static String serialize(Object object) {
        return JSONObject.toJSONString(object);
    }

    /**
     * 反序列化
     *
     * @param json json
     * @param clazz 类
     * @param <T> 泛型
     * @return 反序列化之后
     */
    public static <T> T deserialize(String json, Class<T> clazz) {
        return JSONObject.parseObject(json, clazz);
    }
}
