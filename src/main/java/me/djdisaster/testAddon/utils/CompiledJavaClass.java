package me.djdisaster.testAddon.utils;

import java.lang.reflect.Method;

public class CompiledJavaClass {

    private Class<?> compiledClass;

    public CompiledJavaClass(Class<?> clazz) {
        this.compiledClass = clazz;
    }

    public Object runMethod(String methodName) {
        try {
            Method method = compiledClass.getMethod(methodName);
            return method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object runMethod(String methodName, Object[] args) {
        try {
            Method method = compiledClass.getMethod(methodName);
            return method.invoke(null, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CompiledJavaClassInstance newInstance() {
        try {
            return new CompiledJavaClassInstance(compiledClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }





}
