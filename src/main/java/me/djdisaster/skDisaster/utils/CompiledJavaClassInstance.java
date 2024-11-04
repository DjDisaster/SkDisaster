package me.djdisaster.skDisaster.utils;

import java.lang.reflect.Method;

public class CompiledJavaClassInstance {

    private Object instance;

    public CompiledJavaClassInstance(Object instance) {
        this.instance = instance;
    }

    public Object runMethod(String methodName) {
        Class<?> cls = instance.getClass();
        try {
            Method method = cls.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(instance, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
