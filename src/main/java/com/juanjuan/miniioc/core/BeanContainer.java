package com.juanjuan.miniioc.core;

import com.juanjuan.miniioc.annotation.Component;

import java.lang.annotation.Annotation;
import java.util.HashSet;
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

    // ⭐⭐⭐ AOP 必须：按注解筛选 Bean Types
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> result = new HashSet<>();
        for (Class<?> clazz : beanMap.keySet()) {
            if (clazz.isAnnotationPresent(annotation)) {
                result.add(clazz);
            }
        }
        return result;
    }

    public void injectDependencies() {
        new DependencyInjector(this).doInjection();
    }

    public void addBean(Class<?> clazz, Object bean) {
        beanMap.put(clazz, bean);

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> itf : interfaces) {
            beanMap.putIfAbsent(itf, bean);
        }
    }
}
