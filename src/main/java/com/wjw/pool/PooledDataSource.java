package com.wjw.pool;

import com.wjw.sqlSession.MyConfiguration;

import java.sql.SQLException;

/**
 * @Author: Allen
 * @Description: 数据库连接池实现
 * @Date: Created in 11:25 2018/5/17
 * @Modify By:
 */
public class PooledDataSource {
    private final MyConfiguration configuration = new MyConfiguration();
    private final PoolState state = new PoolState();
    /**
     * 空闲连接数
     */
    public int poolMaximumIdleConnections = 5;

    public void pushConnection(pooledConnection conn) throws SQLException {
        synchronized (state) {
            // 先从活跃线程中删除 conn
            state.activeConnection.remove(conn);
            // 判断连接是否可用
            if (conn.isValid()) {
                // 如果连接数比较少
                if (poolMaximumIdleConnections > state.idelConnection.size()) {
                    // new 一个新的链接，添加到idle列表中
                    pooledConnection connection = new pooledConnection(
                            conn.getRealConnection(), this);
                    // 添加到 空闲连接中
                    state.idelConnection.add(connection);
                    // 标记连接为已被push的连接
                    conn.invalidate();
                    // 通知其他线程前来争抢
                    state.notifyAll();
                } else {     // 空闲连接够多了
                    // 关闭此连接即可
                    conn.getRealConnection().close();
                }
            } else {
                // 坏连接数加一
            }
        }
    }

    public pooledConnection popConnection() {
        pooledConnection conn = null;
        while (true) {   // 不断尝试
            synchronized (state) {
                // 空闲连接池不为空
                if (!state.idelConnection.isEmpty()) {
                    // 删除头的连接，然后返回
                    conn = state.idelConnection.remove(0);
                } else {     // 没有空闲连接
                    // 空闲连接数还没到上限
                    if (poolMaximumIdleConnections > state.idelConnection.size()) {
                        // 新加一个连接
                        conn = new pooledConnection(configuration.getConnection(),
                                this);
                    } else {
                        // 空闲连接数已经够多了 不能再添加了
                        // 淘汰activeConnection 中最老的
                        pooledConnection connection = state.activeConnection.get(0);
                        // 超时的话

                        // 删除连接
                        state.activeConnection.remove(connection);
                        // 新创建一个连接
                        conn = new pooledConnection(connection.getRealConnection(),
                                this);
                        // 标记连接为无效链接
                        connection.invalidate();

                        /*// 时间不够长的话
                        try {
                            // 等待一会，放弃CPU
                            state.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
                if (conn != null) {        // 连接已经拿到
                    // 设置一些属性
                }
            }
            return conn;
        }
    }
}
