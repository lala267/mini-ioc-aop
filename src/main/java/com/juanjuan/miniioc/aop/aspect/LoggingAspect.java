package com.juanjuan.miniioc.aop.aspect;

import com.juanjuan.miniioc.aop.annotation.Aspect;
import com.juanjuan.miniioc.annotation.Component;

import java.lang.reflect.Method;

@Aspect(order = 1, pointcut = "UserService")
@Component   // 建议加上，让它也作为 Bean 被 loadBeans 扫描到
public class LoggingAspect extends DefaultAspect {

    @Override
    public void before(Class<?> targetClass, Method method, Object[] args) {
        System.out.println("[Before] 调用方法: " + targetClass.getSimpleName() + "." + method.getName());
    }

    @Override
    public Object after(Class<?> targetClass, Method method, Object[] args, Object returnValue) {
        System.out.println("[After] 方法结束: " + targetClass.getSimpleName() + "." + method.getName());
        return returnValue;
    }
}
