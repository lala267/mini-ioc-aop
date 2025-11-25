package com.juanjuan.miniioc.service;

import com.juanjuan.miniioc.annotation.Component;
import com.juanjuan.miniioc.annotation.Autowired;

@Component
public class OrderService {

    @Autowired
    private UserService userService;

    public void processOrder() {
        System.out.println("Processing order...");
        userService.hello();
    }
}
