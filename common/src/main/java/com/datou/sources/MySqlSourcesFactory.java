package com.datou.sources;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.net.URL;

/**
 * 数据源配置
 * @author sgz
 * @since 1.0.0 2022/10/26
 */
public class MySqlSourcesFactory {
    /**
     * session工程
     */
    static SqlSessionFactory sqlSessionFactory;

    /**
     * sqlSession对象
     */
    static SqlSession sqlSession;

    static {
        // 定义配置文件，相对路径，文件直接放在resources目录下
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String resource = "dataSources.xml";
        URL url = contextClassLoader.getResource(resource);

        // 防止因编码空格导致无法解析
        String decodedPath = URLUtil.getDecodedPath(url);
        InputStream inputStream = FileUtil.getInputStream(decodedPath);


        // mybatis 读取字节流，利用XMLConfigBuilder类解析文件
        // 将xml文件解析成一个 org.apache.ibatis.session.Configuration 对象
        // 然后将 Configuration 对象交给 SqlSessionFactory 接口实现类 DefaultSqlSessionFactory 管理
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 获取数据库会话
     * @return sqlSession
     */
    public static SqlSession getSqlSession(){
        // openSession 有多个重载方法， 比较重要几个是
        // 1 是否默认提交 SqlSession openSession(boolean autoCommit)
        // 2 设置事务级别 SqlSession openSession(TransactionIsolationLevel level)
        // 3 执行器类型   SqlSession openSession(ExecutorType execType)
        // 不启用事务改用自动提交
        if (sqlSession == null){
            sqlSession = sqlSessionFactory.openSession(true);
        }
        return sqlSession;
    }

}
