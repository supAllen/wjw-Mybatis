package com.wjw.pool;


import java.sql.Connection;

/**
 * @Author: Allen
 * @Description:    每一个连接
 * @Date: Created in 11:28 2018/5/17
 * @Modify By:
 */
public class pooledConnection {
    /**
     * 真正的链接
     */
    private Connection realConnection;
    private PooledDataSource pooledDataSource;

    public pooledConnection(Connection realConnection, PooledDataSource pooledDataSource) {
        this.realConnection = realConnection;
        this.pooledDataSource = pooledDataSource;
    }

    /**
     * 连接是否有效，即看链接是否已关闭
     */
    private boolean valid=true;

    public boolean isValid() {
        return valid;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public void invalidate(){
        valid=false;
    }
}
