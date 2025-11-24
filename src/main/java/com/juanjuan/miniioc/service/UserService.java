package com.juanjuan.miniioc.service;

import com.juanjuan.miniioc.annotation.Component;

@Component
public class UserService {
    public void hello() {
        System.out.println("Hello from UserService");
    }
}
