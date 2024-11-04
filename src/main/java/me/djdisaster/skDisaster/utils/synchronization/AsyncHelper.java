package me.djdisaster.skDisaster.utils.synchronization;

import ch.njol.skript.variables.Variables;
import me.djdisaster.skDisaster.SkDisaster;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncHelper {
    private static final Executor singleThreadExecutor = Executors.newSingleThreadExecutor();

    public static <T> CompletableFuture<T> callSync(Callable<T> callable, Plugin plugin) {
        CompletableFuture<T> future = new CompletableFuture<>();

        Bukkit.getScheduler().callSyncMethod(plugin, () -> {
            try {
                T result = callable.call();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }

            return null;
        });

        return future;
    }

    public static Executor getSingleThreadExecutor() {
        return singleThreadExecutor;
    }

    public static Object getVariableAsync(String key) {
        try {
            return callSync(() -> Variables.getVariable(key, null, false), SkDisaster.getInstance()).get();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}
