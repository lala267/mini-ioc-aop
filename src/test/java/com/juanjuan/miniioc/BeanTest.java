package com.juanjuan.miniioc;

import com.juanjuan.miniioc.core.BeanContainer;
import com.juanjuan.miniioc.service.OrderService;
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

    @Test
    void testDependencyInjection() {
        BeanContainer container = new BeanContainer();
        container.loadBeans("com.juanjuan.miniioc");

        container.injectDependencies(); // ★ 自动注入关键一步

        OrderService orderService = container.getBean(OrderService.class);
        orderService.processOrder();
    }
}
