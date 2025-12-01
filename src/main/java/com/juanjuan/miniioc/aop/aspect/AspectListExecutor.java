package com.juanjuan.miniioc.aop.aspect;

import java.lang.reflect.Method;
import java.util.List;

public class AspectListExecutor {

    private final List<AspectInfo> aspectInfos;

    public AspectListExecutor(List<AspectInfo> aspectInfos) {
        this.aspectInfos = aspectInfos;
    }

    public Object execute(Class<?> targetClass, Method method, Object[] args, Object target) throws Throwable {

        // 1. 执行 before（按order升序）
        for (AspectInfo info : aspectInfos) {
            info.getAspectObject().before(targetClass, method, args);
        }

        Object returnValue = null;
        Throwable throwable = null;

        try {
            // 2. 调用目标方法
            method.setAccessible(true);
            returnValue = method.invoke(target, args);

            // 3. 执行 afterReturning
            for (AspectInfo info : aspectInfos) {
                returnValue = info.getAspectObject().afterReturning(
                        targetClass, method, args, returnValue);
            }

        } catch (Throwable ex) {
            throwable = ex.getCause() == null ? ex : ex.getCause();

            // 4. 执行 afterThrowing
            for (AspectInfo info : aspectInfos) {
                info.getAspectObject().afterThrowing(
                        targetClass, method, args, throwable);
            }

            throw throwable;

        } finally {
            // 5. after 不管如何都执行
            for (AspectInfo info : aspectInfos) {
                info.getAspectObject().after(targetClass, method, args);
            }
        }

        return returnValue;
    }
}
