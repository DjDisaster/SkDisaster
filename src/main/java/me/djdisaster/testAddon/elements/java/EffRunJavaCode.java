package me.djdisaster.testAddon.elements.java;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CompiledJavaClass;
import me.djdisaster.testAddon.utils.CompiledJavaClassInstance;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffRunJavaCode extends Effect {
    static {
        Skript.registerEffect(EffRunJavaCode.class, "run method %string% in %object%");
    }

    private Expression<String> methodName;
    private Expression<Object> compiledJavaClass;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.methodName = (Expression<String>) expressions[0];
        this.compiledJavaClass = (Expression<Object>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";//""Send bungee message to " + players.toString(event, debug) + " with text " + text.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        if (compiledJavaClass.getSingle(event) instanceof CompiledJavaClass) {
            ((CompiledJavaClass) compiledJavaClass.getSingle(event)).runMethod(methodName.getSingle(event));
        } else if (compiledJavaClass.getSingle(event) instanceof CompiledJavaClassInstance) {
            ((CompiledJavaClassInstance) compiledJavaClass.getSingle(event)).runMethod(methodName.getSingle(event));
        } else {
            Bukkit.getLogger().warning("You have tried to run a method on a non-class/instance");
        }
    }
}