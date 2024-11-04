package me.djdisaster.skDisaster.elements.java;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.CompiledJavaClass;
import me.djdisaster.skDisaster.utils.CompiledJavaClassInstance;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprGetClassInstance extends SimpleExpression<CompiledJavaClassInstance> {
    static {
        Skript.registerExpression(
                ExprGetClassInstance.class, CompiledJavaClassInstance.class, ExpressionType.SIMPLE,
                "instance of %object%"
        );
    }

    private Expression<Object> compiledJavaClass;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        compiledJavaClass = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends CompiledJavaClassInstance> getReturnType() {
        return CompiledJavaClassInstance.class;
    }

    @Override
    protected @Nullable CompiledJavaClassInstance[] get(Event event) {
        Object compiledJavaClass = this.compiledJavaClass.getSingle(event);
        if (!(compiledJavaClass instanceof CompiledJavaClass)) {
            Bukkit.getLogger().warning("You have tried to create an instance of a non-class Object");
            return null;
        }
        CompiledJavaClassInstance instance = ((CompiledJavaClass) compiledJavaClass).newInstance();

        return new CompiledJavaClassInstance[]{instance};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "instance of " + compiledJavaClass.toString(event, b);
    }
}