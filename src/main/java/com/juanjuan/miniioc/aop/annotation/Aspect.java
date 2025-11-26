package com.juanjuan.miniioc.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 切面执行顺序，数值越小优先级越高
     */
    int order() default 1;

    /**
     * 非常简化版的切点表达式：
     * 这里只做演示用，约定传入一个要拦截的类名，如 "UserService"
     */
    String pointcut();
}
