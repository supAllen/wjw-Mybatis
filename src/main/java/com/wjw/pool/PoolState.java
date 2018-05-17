package com.wjw.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Allen
 * @Description:    连接池的状态
 * @Date: Created in 12:36 2018/5/17
 * @Modify By:
 */
public class PoolState {
    /**
     * 空闲链接
     */
    protected final List<pooledConnection> idelConnection = new ArrayList<>();

    /**
     * 活动链接
     */
    protected final List<pooledConnection> activeConnection = new ArrayList<>();

    public PoolState() {
    }


}
