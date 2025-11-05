package me.djdisaster.skDisaster.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.LiteralUtils;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.reactivity.Binding;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprNewVar extends SimpleExpression<Binding> {
    static {
        Skript.registerExpression(
                ExprNewVar.class, Binding.class, ExpressionType.SIMPLE,
                "new reactive var[iable] with [value] %object%"
        );
    }

    private Expression<Object> value;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        value = LiteralUtils.defendExpression(exprs[0]);
        return LiteralUtils.canInitSafely(value);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Binding> getReturnType() {
        return Binding.class;
    }

    @Override
    protected @Nullable Binding[] get(Event event) {
        return new Binding[]{
            new Binding(value.getSingle(event))
        };
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "reactive var value " + value.toString(event, b);
    }
}