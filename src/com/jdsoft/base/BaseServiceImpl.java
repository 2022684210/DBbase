package com.jdsoft.base;

import cn.hutool.core.convert.Convert;
import com.jdsoft.exception.BaseServiceException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    //Hutool
    //TODO 连接操作数
    Connection connection;
    String JDBC_URL;
    String JDBC_USER;
    String JDBC_PASSWORD;
    Boolean JDBC_INIT = Boolean.FALSE;

    public BaseServiceImpl<T> init() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(this.JDBC_URL, this.JDBC_USER, this.JDBC_PASSWORD);
            JDBC_INIT = Boolean.TRUE;
        } catch (SQLException e) {
            throw new BaseServiceException("基本服务构建失败");
        }
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_URL) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(JDBC_URL, this.JDBC_USER, this.JDBC_PASSWORD);
            JDBC_INIT = Boolean.TRUE;
        } catch (SQLException e) {
            throw new BaseServiceException("基本服务构建失败");
        }
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_USER,String JDBC_PASSWORD) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(this.JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            JDBC_INIT = Boolean.TRUE;
        } catch (SQLException e) {
            throw new BaseServiceException("基本服务构建失败");
        }
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_URL,String JDBC_USER,String JDBC_PASSWORD) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            JDBC_INIT = Boolean.TRUE;
        } catch (SQLException e) {
            throw new BaseServiceException("基本服务构建失败");
        }
        return this;
    }

    public void isInit() {
        if(!JDBC_INIT){
            throw new BaseServiceException("在此服务上未进行初始化");
        }
    }

    public ResultSet getResultSet(String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatement(String sql, String... params) throws SQLException {
        PreparedStatement szr = connection.prepareStatement(sql);
        for (int i = 1; i <= params.length; i++) {
            szr.setObject(i, params[i - 1]);
        }
        return szr;
    }

    @Override
    public T getById(Integer id) {
        isInit();
        return null;
    }

    @Override
    public List<T> getList(T t) {
        isInit();
        return null;
    }

    @Override
    public void del(T t) {
        isInit();
    }

    @Override
    public void update(T t) {
        isInit();
    }

    @Override
    public void add(T t) {
        //isInit();
        Class<?> tClass = t.getClass();
        for (Field declaredField : tClass.getDeclaredFields()) {
            System.out.println(declaredField.getType());
        }
        BaseDomain temp = Convert.convert(BaseDomain.class,t);
    }
}
