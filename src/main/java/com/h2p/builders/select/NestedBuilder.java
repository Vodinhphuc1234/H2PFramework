package com.h2p.builders.select;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by vodinhphuc on 30/12/2022
 */
public abstract class NestedBuilder<T, V> {
    public T done() {
        Class<?> parentClass = parent.getClass();
        try {
            V build = this.build();
            String methodName = "set" + build.getClass().getSimpleName();
            Method method = parentClass.getDeclaredMethod(methodName, build.getClass());
            method.invoke(parent, build);
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public abstract V build();

    protected T parent;

    public <P extends NestedBuilder<T, V>> P withParentBuilder(T parent) {
        this.parent = parent;
        return (P) this;
    }
}
