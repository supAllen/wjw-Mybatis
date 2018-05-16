package com.wjw.sqlSession;

import com.wjw.mapper.MapperBean;
import com.wjw.pojo.Function;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Allen
 * @Description:    解析配置文件类，以及获取连接
 * @Date: Created in 16:25 2018/5/16
 * @Modify By:
 */
public class MyConfiguration {
    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    /**
     * 获取Connection
     * @param resource
     * @return
     * @throws DocumentException
     * @throws ClassNotFoundException
     */
    public Connection build(String resource) throws DocumentException, ClassNotFoundException {
        InputStream is = loader.getResourceAsStream(resource);
        SAXReader reader = new SAXReader();
        Document document = reader.read(is);
        Element root = document.getRootElement();
        return evalDataSource(root);
    }

    /**
     * 解析配置文件
     * @param root
     * @return
     * @throws ClassNotFoundException
     */
    public Connection evalDataSource(Element root) throws ClassNotFoundException {
        if (!root.getName().equals("database")){
            throw new RuntimeException("root should be database");
        }
        String driverClass=null;
        String url=null;
        String username=null;
        String password=null;
        for (Object m : root.elements("property")){
            Element e = (Element) m;
            String value = getValue(e);
            if (value == null)  throw new NullPointerException("property value is null");
            String name = e.attributeValue("name");
            switch (name){
                case "url": url=value; break;
                case "driverClassName":driverClass=value; break;
                case "username": username=value; break;
                case "password": password=value; break;
                default: throw new RuntimeException("property unknown name");
            }
        }

        Class.forName(driverClass);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获取property属性值，如果有value值就读取，没有就获取其内容
     * @param node
     * @return
     */
    private String getValue(Element node) {
        return node.hasContent() ? node.getText() : node.attributeValue("value");
    }

    /**
     * 解析每个sql
     * @param path
     * @return
     * @throws DocumentException
     */
    public MapperBean readMapper(String path) throws DocumentException {
        MapperBean mapperBean = new MapperBean();
        try {
            InputStream is = loader.getResourceAsStream(path);
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);
            Element root = document.getRootElement();
            mapperBean.setInterfaceName(root.attributeValue("namespace").trim());
            List<Function> list = new ArrayList<>();        // 接口集合
            for (Object element: root.elements()){
                Function function = new Function();     // 存储每一个方法的信息
                Element e = (Element) element;
                String sqlType = e.getName().trim();
                String funcationName = e.attributeValue("id").trim();
                String sql = e.getText().trim();
                String resultType = e.attributeValue("resultType").trim();
                Object newInstance = null;
                try {       // 反射获取实例
                    newInstance = Class.forName(resultType).newInstance();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                function.setFuncName(funcationName);
                function.setSqlType(sqlType);
                function.setResultType(newInstance);        // 将方法返回值存入
                function.setSql(sql);
                list.add(function);
            }
            mapperBean.setList(list);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return mapperBean;
    }
}
