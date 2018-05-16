package com.wjw.sqlSession;

import com.wjw.mapper.MapperBean;
import com.wjw.pojo.Function;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 17:51 2018/5/16
 * @Modify By:
 */
public class MyMapperProxy implements InvocationHandler{
    private MyConfiguration configuration;
    private MySqlSession mySqlSession;

    public MyMapperProxy(MyConfiguration configuration, MySqlSession mySqlSession) {
        this.configuration =configuration;
        this.mySqlSession = mySqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean mapperBean = configuration.readMapper("UserMapper.xml");
        // 是否是xml 文件所对应的接口
        if (!method.getDeclaringClass().getName().equals(mapperBean.getInterfaceName()))
            return null;
        List<Function> list = mapperBean.getList();
        if (null != list || 0 != list.size()){
            for (Function function: list){
                if (method.getName().equals(function.getFuncName())){
                    // 获取配置文件中的  sql和参数
                    return mySqlSession.selectOne(function.getSql(),String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}
