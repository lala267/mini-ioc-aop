package com.juanjuan.miniioc.core;

import com.juanjuan.miniioc.annotation.Autowired;
import java.lang.reflect.Field;
import java.util.Set;

public class DependencyInjector {

    private final BeanContainer beanContainer;

    public DependencyInjector(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    public void doInjection() {
        Set<Class<?>> classes = beanContainer.getClasses();

        for (Class<?> clazz : classes) {
            Object instance = beanContainer.getBean(clazz);
            injectFields(clazz, instance);
        }
    }

    private void injectFields(Class<?> clazz, Object instance) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            Class<?> dependencyType = field.getType();
            Object dependency = beanContainer.getBean(dependencyType);

            if (dependency == null) {
                throw new RuntimeException("No bean found to inject for: " + dependencyType.getName());
            }

            try {
                field.setAccessible(true);
                field.set(instance, dependency);
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject dependency: " + field.getName(), e);
            }
        }
    }
}
