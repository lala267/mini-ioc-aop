package com.juanjuan.miniioc.aop.aspect;

import java.lang.reflect.Method;
import java.util.List;

public class AspectListExecutor {

    // 当前方法对应的所有切面（已经按 order 排序）
    private final List<AspectInfo> aspectInfos;

    public AspectListExecutor(List<AspectInfo> aspectInfos) {
        this.aspectInfos = aspectInfos;
    }

    /**
     * AOP 责任链
     */
    public Object execute(Class<?> targetClass,
                          Method method,
                          Object[] args,
                          Object target) throws Throwable {

        Object returnValue = null;

        try {
            // 1. Before —— 按顺序执行
            for (AspectInfo info : aspectInfos) {
                info.getAspectObject().before(targetClass, method, args);
            }

            // 2. Around —— 使用 ProceedingJoinPoint 执行真实方法
            ProceedingJoinPoint pjp = new ProceedingJoinPoint(target, method, args);
            returnValue = pjp.proceed();   // 执行目标方法（代替 method.invoke）

            // 3. AfterReturning —— 处理返回值（可链式加工）
            for (AspectInfo info : aspectInfos) {
                returnValue = info.getAspectObject()
                        .afterReturning(targetClass, method, args, returnValue);
            }

        } catch (Throwable ex) {

            // 4. AfterThrowing —— 方法异常时执行
            for (AspectInfo info : aspectInfos) {
                info.getAspectObject()
                        .afterThrowing(targetClass, method, args, ex);
            }

            throw ex; // 不吞异常，继续抛出

        } finally {

            // 5. After —— 无论成功失败都执行
            for (AspectInfo info : aspectInfos) {
                info.getAspectObject().after(targetClass, method, args);
            }
        }

        return returnValue;
    }
}
