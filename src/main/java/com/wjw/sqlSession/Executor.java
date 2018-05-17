package com.wjw.sqlSession;

/**
 * @Author: Allen
 * @Description:    执行器的接口
 * @Date: Created in 17:44 2018/5/16
 * @Modify By:
 */
public interface Executor {
    <T> T query(String statment, Object paramter);
}
