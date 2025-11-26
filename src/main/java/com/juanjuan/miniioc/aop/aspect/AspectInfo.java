package com.juanjuan.miniioc.aop.aspect;

public class AspectInfo {

    private int orderIndex;
    private DefaultAspect aspectObject;

    public AspectInfo(int orderIndex, DefaultAspect aspectObject) {
        this.orderIndex = orderIndex;
        this.aspectObject = aspectObject;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public DefaultAspect getAspectObject() {
        return aspectObject;
    }
}
