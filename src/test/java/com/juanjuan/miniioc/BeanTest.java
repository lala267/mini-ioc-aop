package com.juanjuan.miniioc;

import com.juanjuan.miniioc.aop.aspect.AspectWeaver;
import com.juanjuan.miniioc.core.BeanContainer;
import com.juanjuan.miniioc.service.OrderService;
import com.juanjuan.miniioc.service.UserService;
import com.juanjuan.miniioc.service.UserServiceInterface;
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

    @Test
    void testAOP() {
        BeanContainer container = new BeanContainer();
        container.loadBeans("com.juanjuan.miniioc");

        // 依赖注入
        container.injectDependencies();

        // 织入 AOP
        new AspectWeaver(container).doAOP();

        // 打印容器所有 bean，看是否被代理
//        container.getClasses().forEach(c -> {
//            System.out.println(c + " -> " + container.getBean(c));
//        });

        UserServiceInterface userService = container.getBean(UserServiceInterface.class);
        userService.hello();
    }
}
