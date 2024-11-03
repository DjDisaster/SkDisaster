package me.djdisaster.testAddon.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.synchronization.AsyncHelper;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprVariableAsync extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(
                ExprVariableAsync.class, Object.class, ExpressionType.SIMPLE,
                "variable %string% async"
        );
    }

    private Expression<String> variableName;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        variableName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Object> getReturnType() {
        return Object.class;
    }

    @Override
    protected @Nullable Object[] get(Event event) {
        return new Object[]{AsyncHelper.getVariableAsync(variableName.getSingle(event))};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "variable " + variableName.toString(event, b) + " async";
    }
}