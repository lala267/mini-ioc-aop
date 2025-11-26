package com.juanjuan.miniioc.aop.aspect;

import java.lang.reflect.Method;
import java.util.List;

public class AspectListExecutor {

    private final List<AspectInfo> aspectInfos;

    public AspectListExecutor(List<AspectInfo> aspectInfos) {
        this.aspectInfos = aspectInfos;
    }

    public Object execute(Class<?> targetClass, Method method, Object[] args, Object targetObject) throws Throwable {
        // 1. before
        for (AspectInfo aspectInfo : aspectInfos) {
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }

        // 2. 执行实际方法
        Object returnValue = method.invoke(targetObject, args);

        // 3. after
        for (AspectInfo aspectInfo : aspectInfos) {
            returnValue = aspectInfo.getAspectObject().after(targetClass, method, args, returnValue);
        }

        return returnValue;
    }
}
