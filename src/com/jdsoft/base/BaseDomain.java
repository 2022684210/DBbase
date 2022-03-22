package com.jdsoft.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseDomain implements Serializable {
    List<String> list = new ArrayList<>();

    protected BaseDomain() {
    }

    protected BaseDomain(String e) {
        add(e);
    }

    protected BaseDomain(String... e) {
        addAll(e);
    }

    protected BaseDomain(List<String> list) {
        this.list = list;
    }

    protected void add(String e) {
        list.add(e);
    }

    protected final void addAll(String... list) {
        Collections.addAll(this.list, list);
    }
}
