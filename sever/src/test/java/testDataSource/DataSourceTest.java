package testDataSource;


import com.datou.entity.User;
import com.datou.entity.mapper.UserMapper;
import com.datou.sources.MySqlSourcesFactory;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.*;

public class DataSourceTest {

    @Test
    public void test2(){
        SqlSession sqlSession = MySqlSourcesFactory.getSqlSession();
        UserMapper mapper =  sqlSession.getMapper(UserMapper.class);
        /* insert */
        User user = new User();
        user.setUsername("LiuYork");
        user.setPassword("123456");
        mapper.insert(user);
    }

}
