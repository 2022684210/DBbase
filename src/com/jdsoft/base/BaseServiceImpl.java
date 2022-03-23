package com.jdsoft.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.jdsoft.exception.BaseServiceException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    //Hutool
    //TODO 连接操作数
    Connection connection;
    private final SimpleStringProperty JDBC_URL = new SimpleStringProperty("");//数据库地址与端口
    private final SimpleStringProperty JDBC_SOURCE = new SimpleStringProperty("");//地址源
    private final SimpleStringProperty JDBC_USER = new SimpleStringProperty("");//账号
    private final SimpleStringProperty JDBC_PASSWORD = new SimpleStringProperty("");//账号密码
    private final SimpleBooleanProperty JDBC_USE_UNICODE =new SimpleBooleanProperty(Boolean.FALSE);//编码统一启用状态
    private final SimpleStringProperty JDBC_UNICODE = new SimpleStringProperty("UTF-8");//编码类型
    private final SimpleBooleanProperty JDBC_USE_SSL =new SimpleBooleanProperty(Boolean.FALSE);//SSL启用状态
    private final SimpleStringProperty JDBC_SERVER_TIME_ZONE = new SimpleStringProperty("UTC");//服务器时区
    private final SimpleStringProperty DRIVER = new SimpleStringProperty("mysql");//驱动程序
    private String DriverName = "com.mysql.cj.jdbc.Driver";//驱动路径
    public SimpleStringProperty TABLE_TARGET = new SimpleStringProperty("");//表目标

    Boolean JDBC_INIT = Boolean.FALSE;//初始化状态
    String JDBC_CONNECT;
    {
        JDBC_URL.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_SOURCE.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_USER.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_PASSWORD.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_USE_UNICODE.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_UNICODE.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_USE_SSL.addListener((observable, oldValue, newValue) -> {
            again();
        });
        JDBC_SERVER_TIME_ZONE.addListener((observable, oldValue, newValue) -> {
            again();
        });
        DRIVER.addListener((observable, oldValue, newValue) -> {
            switch (newValue){
                case "mysql", "Mysql", "MySQL" ->{
                    DriverName = "com.mysql.cj.jdbc.Driver";
                }
            }
            again();
        });
    }
    protected void again(){
        if(connection!=null)
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(JDBC_INIT)
            init();
    }


    public BaseServiceImpl<T> setEncoding(boolean isEncoding){
        this.JDBC_USE_UNICODE.set(isEncoding);
        return this;
    }
    public BaseServiceImpl<T> setEncoding(boolean isEncoding,String encoding){
        this.JDBC_USE_UNICODE.set(isEncoding);
        this.JDBC_UNICODE.set(encoding);
        return this;
    }
    public BaseServiceImpl<T> setSSL(boolean isSSL){
        this.JDBC_USE_SSL.set(isSSL);
        return this;
    }
    public BaseServiceImpl<T> setTimeZone(String timeZone){
        this.JDBC_SERVER_TIME_ZONE.set(timeZone);
        return this;
    }
    public BaseServiceImpl<T> setUser(String user,String password){
        this.JDBC_USER.set(user);
        this.JDBC_PASSWORD.set(password);
        return this;
    }
    public BaseServiceImpl<T> setSource(String source){
        this.JDBC_SOURCE.set(source);
        return this;
    }
    public BaseServiceImpl<T> setSource(String url, String source){
        this.JDBC_URL.set(url);
        this.JDBC_SOURCE.set(source);
        return this;
    }
    public BaseServiceImpl<T> setUrl(String url){
        this.JDBC_URL.set(url);
        return this;
    }
    public BaseServiceImpl<T> setDriver(String driver){
        this.DRIVER.set(driver);
        return this;
    }
    public BaseServiceImpl<T> setTableTarget(String tableTarget){
        this.TABLE_TARGET.set(tableTarget);
        return this;
    }

    public BaseServiceImpl<T> init() {
        connect();
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_URL) {
        this.JDBC_URL.set(JDBC_URL);
        connect();
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_USER,String JDBC_PASSWORD) {
        this.JDBC_USER.set(JDBC_USER);
        this.JDBC_PASSWORD.set(JDBC_PASSWORD);
        connect();
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_URL,String JDBC_USER,String JDBC_PASSWORD) {
        this.JDBC_URL.set(JDBC_URL);
        this.JDBC_USER.set(JDBC_USER);
        this.JDBC_PASSWORD.set(JDBC_PASSWORD);
        connect();
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_URL,String JDBC_SOURCE,String JDBC_USER,String JDBC_PASSWORD) {
        this.JDBC_URL.set(JDBC_URL);
        this.JDBC_SOURCE.set(JDBC_SOURCE);
        this.JDBC_USER.set(JDBC_USER);
        this.JDBC_PASSWORD.set(JDBC_PASSWORD);
        connect();
        return this;
    }
    public BaseServiceImpl<T> init(String JDBC_URL,String JDBC_SOURCE,String JDBC_USER,String JDBC_PASSWORD,String TABLE_TARGET) {
        this.JDBC_URL.set(JDBC_URL);
        this.JDBC_SOURCE.set(JDBC_SOURCE);
        this.JDBC_USER.set(JDBC_USER);
        this.JDBC_PASSWORD.set(JDBC_PASSWORD);
        this.TABLE_TARGET.set(TABLE_TARGET);
        connect();
        return this;
    }

    private void connect(){
        this.JDBC_CONNECT = "jdbc:mysql://"+this.JDBC_URL.get()+"/"+this.JDBC_SOURCE.get()+"?useUnicode="+this.JDBC_USE_UNICODE.get()+"&characterEncoding="+this.JDBC_UNICODE.get()+"&useSSL="+this.JDBC_USE_SSL.get()+"&serverTimezone="+this.JDBC_SERVER_TIME_ZONE.get();
        try {
            Class.forName(DriverName).getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(this.JDBC_CONNECT, this.JDBC_USER.get(), this.JDBC_PASSWORD.get());
            JDBC_INIT = Boolean.TRUE;
        } catch (SQLException e) {
            throw new BaseServiceException("基本服务构建失败");
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new BaseServiceException("未找到对应的驱动程序");
        }
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

    public PreparedStatement getPreparedStatement(String sql, Object... params) throws SQLException {
        PreparedStatement szr = connection.prepareStatement(sql);
        for (int i = 1; i <= params.length; i++) {
            szr.setObject(i, params[i - 1]);
        }
        return szr;
    }

    @Override
    public void addLibrary(String libraryName){
        try {
            getPreparedStatement("CREATE DATABASE "+libraryName).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        isInit();
        Class<?> tClass = t.getClass();
        HashMap<String,Object> fieldValue = new HashMap<>();
        for (Field declaredField : tClass.getDeclaredFields()) {
            try {
                declaredField.setAccessible(true);
                fieldValue.put(declaredField.getName(), declaredField.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        StringBuilder cmd = new StringBuilder("INSERT INTO "+TABLE_TARGET.get()+" (");
        ArrayList<Object> values = new ArrayList<>();
        fieldValue.forEach((k,y)->{
            cmd.append(k).append(",");
            values.add(y);
        });
        cmd.deleteCharAt(cmd.length()-1).append(") VALUES (").append(optional(fieldValue)).append(")");
        try {
            getPreparedStatement(cmd.toString(),values.toArray()).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static String optional(HashMap<String,Object> fieldValue){
        StringBuilder sum = new StringBuilder();
        for (int i = 0; i < fieldValue.size(); i++) {
            sum.append("?");
            if(i!=fieldValue.size()-1) {
                sum.append(",");
            }
        }
        return sum.toString();
    }
}
