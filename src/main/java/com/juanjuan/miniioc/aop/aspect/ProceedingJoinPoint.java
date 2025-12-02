package com.juanjuan.miniioc.aop.aspect;

import java.lang.reflect.Method;

public class ProceedingJoinPoint {

    private final Object target;
    private final Method method;
    private final Object[] args;

    public ProceedingJoinPoint(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Object proceed() throws Throwable {
        return method.invoke(target, args);
    }
}
