package com.jdsoft.base;

import java.util.List;

public interface BaseService <T>{
    T getById(Integer id);
    List<T> getList(T t);
    void del(T t);
    void update(T t);
    void add(T t);
}
