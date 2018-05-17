package com.wjw.sqlSession;

import com.wjw.pojo.User;
import com.wjw.pool.PooledDataSource;
import com.wjw.pool.pooledConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Allen
 * @Description: 执行器接口的实现类
 * @Date: Created in 17:46 2018/5/16
 * @Modify By:
 */
public class MyExector implements Executor {
    private PooledDataSource pooledDataSource = new PooledDataSource();

    /**
     * @param sql      sql语句
     * @param paramter 语句中的参数
     * @param <T>
     * @return
     */
    @Override
    public <T> T query(String sql, Object paramter) {
        pooledConnection conn = pooledDataSource.popConnection();
        Connection connection = conn.getRealConnection();
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            // 设置参数
            statement.setString(1, paramter.toString());
            resultSet = statement.executeQuery();
            User user = new User();
            if (resultSet.next()) {
                user.setId(resultSet.getString(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
            }
            return (T) user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null){
                    pooledDataSource.pushConnection(conn);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
