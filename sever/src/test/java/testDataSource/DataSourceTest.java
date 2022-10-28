package testDataSource;


import com.datou.entity.User;
import com.datou.entity.mapper.UserMapper;
import com.datou.sources.DataSources;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataSourceTest {

    @org.junit.Test
    public void test1() throws IOException {
        DataSources dataSources = new DataSources();
        dataSources.init();
        SqlSession session = dataSources.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = new User();
        user.setUserName("a");
        user.setPassword("b");
        mapper.insert(user);
    }

    @Test
    public void test2() throws IOException {
        // 定义配置文件，相对路径，文件直接放在resources目录下
        String resource = "datasources.xml";
        // 读取文件字节流
        InputStream inputStream = Resources.getResourceAsStream(resource);

        // mybatis 读取字节流，利用XMLConfigBuilder类解析文件
        // 将xml文件解析成一个 org.apache.ibatis.session.Configuration 对象
        // 然后将 Configuration 对象交给 SqlSessionFactory 接口实现类 DefaultSqlSessionFactory 管理
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // openSession 有多个重载方法， 比较重要几个是
        // 1 是否默认提交 SqlSession openSession(boolean autoCommit)
        // 2 设置事务级别 SqlSession openSession(TransactionIsolationLevel level)
        // 3 执行器类型   SqlSession openSession(ExecutorType execType)
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // mybatis 内部其实已经解析好了 mapper 和 mapping 对应关系，放在一个map中，这里可以直接获取
        // 如果看源码可以发现userMapper 其实是一个代理类MapperProxy，
        // 通过 sqlSession、mapperInterface、mechodCache三个参数构造的
        // MapperProxyFactory 类中 newInstance(MapperProxy<T> mapperProxy)方法
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        /* insert */
        User user = new User();
        user.setUserName("LiuYork");
        user.setPassword("123456");
        userMapper.insert(user);
        // 由于默认 openSession() 事务是交由开发者手动控制，所以需要显示提交
        sqlSession.commit();

    }

}
