package me.djdisaster.skDisaster.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.reactivity.Binding;
import me.djdisaster.skDisaster.utils.reactivity.BindingSkriptListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ExprListenersOfReactiveVar extends SimpleExpression<BindingSkriptListener> {
    static {
        Skript.registerExpression(
                ExprListenersOfReactiveVar.class, BindingSkriptListener.class, ExpressionType.SIMPLE,
                "[reactive] listeners of %object%"
        );
    }

    private Expression<Object> bindable;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        bindable = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends BindingSkriptListener> getReturnType() {
        return BindingSkriptListener.class;
    }

    @Override
    protected @Nullable BindingSkriptListener[] get(Event event) {

        Binding binding = (Binding) bindable.getSingle(event);
        if (binding == null) {
            Bukkit.getLogger().warning("ExprListenersOfReactiveVar called with null");
            return null;
        }
        return binding.getListeners().toArray(new BindingSkriptListener[0]);
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "bindable of " + bindable.toString(event, b);
    }
}