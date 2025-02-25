package me.djdisaster.skDisaster.elements.java;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionList;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExprRunJavaCode extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(
                ExprRunJavaCode.class, Object.class, ExpressionType.SIMPLE,
                "run method %string% in %object% [with %-objects%]"
        );
    }

    private Expression<String> methodName;
    private Expression<Object> compiledJavaClass;
    private List<Expression<?>> arguments;
    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        methodName = (Expression<String>) exprs[0];
        compiledJavaClass = (Expression<Object>) exprs[1];
        arguments = new ArrayList<>();
        if (exprs[2] instanceof ExpressionList) {
            Collections.addAll(arguments, ((ExpressionList<?>) exprs[2]).getExpressions());
        } else if (exprs[2] != null) {
            arguments.add(exprs[2]);
        }
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
        Object compiledJavaClass = this.compiledJavaClass.getSingle(event);
        String methodName = this.methodName.getSingle(event);

        Object[] args = new Object[arguments.size()];

        for (int i = 0; i < arguments.size(); i++) {
            var array = arguments.get(i).getArray(event);
            if (array.length == 1) {
                args[i] = array[0];
            } else {
                args[i] = array;
            }
        }

        if (compiledJavaClass instanceof CompiledJavaClass) {
            return new Object[]{
                    ((CompiledJavaClass) compiledJavaClass).runMethod(methodName, args)
            };
        } else if (compiledJavaClass instanceof CompiledJavaClassInstance) {
            return new Object[]{
                    ((CompiledJavaClassInstance) compiledJavaClass).runMethod(methodName, args)
            };
        } else {
            Bukkit.getLogger().warning("You have tried to run a method on a non-class/instance");
        }
        return null;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "run method " + methodName.toString(event, b) + " in " + compiledJavaClass.toString(event, b);
    }
}