package com.jdsoft.test;

import com.jdsoft.base.BaseService;
import com.jdsoft.domain.Messge;
import com.jdsoft.domain.User;
import com.jdsoft.service.MessgeService;
import com.jdsoft.service.UserService;

public class Test {
    private static BaseService<User> userService = new UserService();
    private static BaseService<Messge> messgeService = new MessgeService();
    public static void main(String[] args) {
        UserService tp = new UserService();
        tp.init("localhost:3306","user","root","root");
//        userService.getById(2);
    }
}
