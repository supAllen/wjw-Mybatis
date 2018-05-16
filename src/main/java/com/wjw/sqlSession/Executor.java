package com.wjw.sqlSession;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 17:44 2018/5/16
 * @Modify By:
 */
public interface Executor {
    <T> T query(String statment, Object paramter);
}
