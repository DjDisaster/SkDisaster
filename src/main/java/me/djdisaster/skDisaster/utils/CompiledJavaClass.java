package me.djdisaster.skDisaster.utils;

import org.bukkit.Bukkit;

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
            var params = new Class<?>[args.length];


            for (int i = 0; i < args.length; i++) {
                params[i] = args[i].getClass();
            }


            Method method = compiledClass.getMethod(methodName, params);
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
