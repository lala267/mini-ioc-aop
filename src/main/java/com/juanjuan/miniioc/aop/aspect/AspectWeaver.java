package com.juanjuan.miniioc.aop.aspect;

import com.juanjuan.miniioc.aop.annotation.Aspect;
import com.juanjuan.miniioc.core.BeanContainer;

import java.lang.reflect.Proxy;
import java.util.*;

public class AspectWeaver {

    private final BeanContainer beanContainer;

    public AspectWeaver(BeanContainer container) {
        this.beanContainer = container;
    }

    public void doAOP() {
        // 1. 获取所有切面类
        Set<Class<?>> aspectClasses = beanContainer.getClassesByAnnotation(Aspect.class);
        if (aspectClasses == null || aspectClasses.isEmpty()) return;

        // 2. 遍历每个 @Aspect 类
        for (Class<?> aspectClass : aspectClasses) {

            Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
            int order = aspectTag.order();
            String pointcut = aspectTag.pointcut();

            try {
                // 2.2 创建切面对象
                DefaultAspect aspectObject =
                        (DefaultAspect) aspectClass.getDeclaredConstructor().newInstance();

                // 2.3 构建切面链表
                List<AspectInfo> aspectInfos = new ArrayList<>();
                aspectInfos.add(new AspectInfo(order, aspectObject));

                // Step5: 按 order 排序
                aspectInfos.sort(Comparator.comparingInt(AspectInfo::getOrderIndex));

                // 3. 遍历所有 Bean，寻找目标类
                Set<Class<?>> classSet = beanContainer.getClasses();
                if (classSet == null) continue;

                for (Class<?> targetClass : classSet) {
                    if (!targetClass.getSimpleName().contains(pointcut)) continue;

                    Object targetBean = beanContainer.getBean(targetClass);
                    if (targetBean == null) continue;

                    // 4. 创建 JDK 代理
                    Object proxyBean = Proxy.newProxyInstance(
                            targetClass.getClassLoader(),
                            targetClass.getInterfaces(),
                            (proxy, method, args) -> {
                                AspectListExecutor executor = new AspectListExecutor(aspectInfos);
                                return executor.execute(targetClass, method, args, targetBean);
                            }
                    );

                    // 5. 覆盖 Bean（类 + 接口）
                    beanContainer.addBean(targetClass, proxyBean);

                    for (Class<?> itf : targetClass.getInterfaces()) {
                        beanContainer.addBean(itf, proxyBean);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
