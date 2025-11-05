package me.djdisaster.skDisaster.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.ReadImage;
import me.djdisaster.skDisaster.utils.reactivity.BindingSkriptListener;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

// im bad at naming files ok :(
public class ExprInfoOfBindingSkriptListener extends SimpleExpression<String> {
    static {
        Skript.registerExpression(
                ExprInfoOfBindingSkriptListener.class, String.class, ExpressionType.SIMPLE,
                "(1:id|2:function) of %bindinglistener%"
        );
    }

    private Expression<BindingSkriptListener> bindingListener;
    private Kleenean isId;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        bindingListener = (Expression<BindingSkriptListener>) exprs[0];

        isId = (parseResult.mark == 1) ? Kleenean.TRUE : Kleenean.FALSE;
        return true;
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @Nullable String[] get(Event event) {
        BindingSkriptListener listener = bindingListener.getSingle(event);
        if (listener == null) {
            Bukkit.getLogger().warning("ExprInfoOfBindingSkriptListener called with null.");
            return null;
        }

        if (isId.isTrue()) {
            return new String[]{listener.getId()};
        }
        return new String[]{listener.getFunctionName()};

    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "ExprListenersOfReactiveVar called with " + bindingListener.getSingle(event);
    }

}
