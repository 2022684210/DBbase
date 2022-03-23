package com.jdsoft.domain;

import com.jdsoft.base.BaseDomain;

public class User extends BaseDomain {
    String tmd = "傻子";
    User to = null;
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
