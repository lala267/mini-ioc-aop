package com.juanjuan.miniioc;

import com.juanjuan.miniioc.core.BeanContainer;
import com.juanjuan.miniioc.service.UserService;
import org.junit.jupiter.api.Test;

public class BeanTest {

    @Test
    void testLoadBeans() {
        BeanContainer container = new BeanContainer();
        container.loadBeans("com.juanjuan.miniioc");

        UserService userService = container.getBean(UserService.class);
        userService.hello();
    }
}
