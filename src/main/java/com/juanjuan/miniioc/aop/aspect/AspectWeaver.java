package com.juanjuan.miniioc.aop.aspect;

import com.juanjuan.miniioc.aop.annotation.Aspect;
import com.juanjuan.miniioc.core.BeanContainer;

import java.lang.reflect.Proxy;
import java.util.*;

public class AspectWeaver {

    private final BeanContainer beanContainer;

    public AspectWeaver(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    public void doAOP() {
        Set<Class<?>> classSet = beanContainer.getClasses();
        if (classSet == null || classSet.isEmpty()) {
            return;
        }

        // 1. 找出所有切面类（标了 @Aspect 的类）
        for (Class<?> clazz : classSet) {
            if (!clazz.isAnnotationPresent(Aspect.class)) {
                continue;
            }

            Aspect aspectTag = clazz.getAnnotation(Aspect.class);
            int order = aspectTag.order();
            String pointcut = aspectTag.pointcut();

            try {
                DefaultAspect aspectObject =
                        (DefaultAspect) clazz.getDeclaredConstructor().newInstance();

                List<AspectInfo> aspectInfos =
                        Collections.singletonList(new AspectInfo(order, aspectObject));

                // 2. 扫描所有 bean，判断是否匹配 pointcut
                for (Class<?> targetClass : classSet) {

                    if (!matchPointcut(pointcut, targetClass)) {
                        continue;
                    }

                    Object targetBean = beanContainer.getBean(targetClass);
                    if (targetBean == null) {
                        continue;
                    }

                    Object proxy = createProxy(targetBean, aspectInfos);

                    // 3. 使用代理对象替换原始 bean
                    beanContainer.addBean(targetClass, proxy);

                    // ⭐⭐ 新增：让接口也指向代理对象 ⭐⭐
                    Class<?>[] interfaces = targetClass.getInterfaces(); // 这里要用 targetClass！
                    for (Class<?> itf : interfaces) {
                        beanContainer.addBean(itf, proxy);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to weave aspects", e);
            }
        }
    }

    /**
     * 非常简化版匹配：只要 pointcut 字符串包含目标类的简单类名就认为匹配
     * 例如：pointcut = "UserService"，targetClass.getSimpleName() = "UserService"
     */
    private boolean matchPointcut(String pointcutExpression, Class<?> targetClass) {
        String simpleName = targetClass.getSimpleName();
        return pointcutExpression.contains(simpleName);
    }

    private Object createProxy(Object target, List<AspectInfo> aspectInfos) {
        Class<?> targetClass = target.getClass();
        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces.length == 0) {
            // 教学版：如果没有接口，就直接用类本身。真实 Spring 这里会用 CGLIB，我们就不管那么细了
            interfaces = new Class<?>[]{targetClass};
        }

        AspectListExecutor executor = new AspectListExecutor(aspectInfos);

        return Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                interfaces,
                (proxy, method, args) -> {
                    // 排除 Object.class 的方法，不要拦截 toString、hashCode
                    if (method.getDeclaringClass() == Object.class) {
                        return method.invoke(target, args);
                    }
                    return executor.execute(targetClass, method, args, target);
                }
        );

    }
}
