package com.juanjuan.miniioc.aop.aspect;

import com.juanjuan.miniioc.aop.annotation.Aspect;
import com.juanjuan.miniioc.annotation.Component;

import java.lang.reflect.Method;

@Component
@Aspect(order = 1, pointcut = "UserService")
public class LoggingAspect extends DefaultAspect {

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) {
        System.out.println("[Before] 正在调用：" + targetClass.getSimpleName() + "." + method.getName());
    }

    @Override
    public Object afterReturning(Class<?> targetClass, Method method, Object[] args, Object returnValue) {
        System.out.println("[AfterReturning] 返回值：" + returnValue);
        return returnValue;
    }

    @Override
    public void afterThrowing(Class<?> targetClass, Method method, Object[] args, Throwable throwable) {
        System.out.println("[AfterThrowing] 方法异常：" + throwable.getMessage());
    }

    @Override
    public void after(Class<?> targetClass, Method method, Object[] args) {
        System.out.println("[After] 方法执行完毕：" + method.getName());
    }
}
