package com.juanjuan.miniioc.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pointcut {
    String value();   // 切点表达式，例如："execution(com.xxx.UserService.*)"
}
