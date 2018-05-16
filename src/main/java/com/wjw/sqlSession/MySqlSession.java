package com.wjw.sqlSession;

import java.lang.reflect.Proxy;

/**
 * @Author: Allen
 * @Description:    一个SqlSession 对应一次sql操作
 * @Date: Created in 17:43 2018/5/16
 * @Modify By:
 */
public class MySqlSession {
    private Executor executor = new MyExector();

    private MyConfiguration configuration = new MyConfiguration();

    public <T> T selectOne(String statment, Object paramter){
        return executor.query(statment,paramter);
    }

    public <T> T getMapper(Class clas){
        return (T) Proxy.newProxyInstance(clas.getClassLoader(),new Class[]{clas},
                new MyMapperProxy(configuration,this));
    }
}
