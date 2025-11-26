package com.juanjuan.miniioc.aop.aspect;

public class DefaultAspect {

    public void before(Class<?> targetClass, java.lang.reflect.Method method, Object[] args) throws Throwable {}

    public Object after(Class<?> targetClass, java.lang.reflect.Method method, Object[] args, Object returnValue) throws Throwable {
        return returnValue;
    }
}
