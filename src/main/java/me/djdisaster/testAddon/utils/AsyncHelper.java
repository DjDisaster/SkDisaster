package me.djdisaster.testAddon.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class AsyncHelper {

    private final Plugin plugin;

    public AsyncHelper(Plugin plugin) {
        this.plugin = plugin;
    }

    public <T> CompletableFuture<T> callSync(Callable<T> callable) {
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

}
