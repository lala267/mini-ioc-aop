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
                    beanMap.put(clazz, instance);
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

}
