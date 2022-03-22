package com.jdsoft.domain;

import com.jdsoft.base.BaseDomain;

public class User extends BaseDomain {
    public String tmd = "傻子";
    User to;
    public User() {
        super();
    }
    public User(String e) {
        super(e);
    }
    public User(String... init){
        super(init);
    }
}
