package me.djdisaster.skDisaster.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.reactivity.Binding;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprGetValue extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(
                ExprGetValue.class, Object.class, ExpressionType.SIMPLE,
                "value of %object%"
        );
    }

    private Expression<Object> binding;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        binding = (Expression<Object>) exprs[0];
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

        if (binding.getSingle(event) == null) {
            return null;
        }

        if (!(this.binding.getSingle(event) instanceof Binding.Impl<?>)) {
            return null;
        }

        return new Object[]{
                ((Binding<?>) binding.getSingle(event)).get()
        };
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "reactive var get value of " + binding.toString(event, b);
    }
}