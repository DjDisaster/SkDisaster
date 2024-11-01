package me.djdisaster.testAddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class TestAddon extends JavaPlugin {

    private static TestAddon instance;
    private SkriptAddon addon;

    @Override
    public void onEnable() {
        Plugin skriptPlugin = getServer().getPluginManager().getPlugin("Skript");
        if (skriptPlugin == null) {
            getLogger().severe("Skript not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!skriptPlugin.isEnabled()) {
            getLogger().severe("Skript is not enabled! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!Skript.isAcceptRegistrations()) {
            getLogger().severe("Skript is not accepting registrations! Cannot load addon anymore. Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        addon = Skript.registerAddon(this);
        try {
            addon.loadClasses("me.djdisaster.testAddon", "elements");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TestAddon getInstance() {
        return instance;
    }
}