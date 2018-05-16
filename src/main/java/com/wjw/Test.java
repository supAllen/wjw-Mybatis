package com.wjw;

import com.wjw.dao.UserMapper;
import com.wjw.pojo.User;
import com.wjw.sqlSession.MySqlSession;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 19:49 2018/5/16
 * @Modify By:
 */
public class Test {
    public static void main(String[] args) {
        MySqlSession sqlSession = new MySqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById("1001");
        System.out.println(user);
    }
}
