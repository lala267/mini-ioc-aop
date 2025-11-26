package com.juanjuan.miniioc.core;

import com.juanjuan.miniioc.annotation.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContainer {

    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    public void loadBeans(String basePackage) {
        Set<Class<?>> classSet = ClassScanner.scanPackage(basePackage);

        for (Class<?> clazz : classSet) {
            if (clazz.isAnnotationPresent(Component.class)) {
                try {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    // 关键：统一走 addBean，顺带把接口也注册进容器
                    addBean(clazz, instance);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create bean: " + clazz.getName(), e);
                }
            }
        }
    }


    public <T> T getBean(Class<T> clazz) {
        return (T) beanMap.get(clazz);
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    public void injectDependencies() {
        new DependencyInjector(this).doInjection();
    }

    public void addBean(Class<?> clazz, Object bean) {
        // 1. 先注册实现类
        beanMap.put(clazz, bean);

        // 2. 再把它实现的所有接口也注册到容器
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> itf : interfaces) {
            // 如果这个接口还没被注册，就指向当前 bean
            beanMap.putIfAbsent(itf, bean);
        }
    }
}
