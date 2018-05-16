package com.wjw.mapper;

import com.wjw.pojo.Function;

import java.util.List;

/**
 * @Author: Allen
 * @Description:
 * @Date: Created in 16:23 2018/5/16
 * @Modify By:
 */
public class MapperBean {
    private String interfaceName;   // 接口名
    private List<Function> list;    // 接口下所有的方法

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<Function> getList() {
        return list;
    }

    public void setList(List<Function> list) {
        this.list = list;
    }
}
