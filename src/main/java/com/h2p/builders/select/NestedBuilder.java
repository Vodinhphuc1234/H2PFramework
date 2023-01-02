package com.h2p.builders.select;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public abstract class NestedBuilder<T, V> {
    public T done() {
        Class<?> parentClass = parent.getClass();
        try {
            V build = this.build();
            String methodName = "set" + build.getClass().getSimpleName();
            Method method = parentClass.getDeclaredMethod(methodName, build.getClass());
            method.setAccessible(true);
            method.invoke(parent, build);
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
        return parent;
    }

    protected abstract V build();

    protected T parent;

    public <P extends NestedBuilder<T, V>> P withParentBuilder(T parent) {
        this.parent = parent;
        return (P) this;
    }
}
