package me.djdisaster.testAddon.utils;

import ch.njol.skript.variables.Variables;
import me.djdisaster.testAddon.TestAddon;

import java.util.concurrent.*;

public class AsyncManager {

    private static final Executor singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static final AsyncHelper asyncHelper = new AsyncHelper(TestAddon.getInstance());
    public static Executor getSingleThreadExecutor() {
        return singleThreadExecutor;
    }

    public static Object getVariableAsync(String key) {
        try {
            return asyncHelper.callSync(() -> Variables.getVariable(key, null, false)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
