package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.SkDisaster;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public abstract class AsyncListStoreEffect extends Effect {

    protected Variable<?> variable;

    protected abstract String getKey(Event event);

    protected abstract void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException;

    protected abstract Object[] getValues(Event event, String key);

    protected abstract boolean initExpressions(Expression<?>[] exprs);

    @Override
    protected void execute(Event event) { }

    @Override
    protected @Nullable TriggerItem walk(Event event) {
        debug(event, true);

        TriggerItem next = getNext();
        Delay.addDelayedEvent(event);

        String key = getKey(event);

        VelocityUtils.fetch(
                key,
                () -> Bukkit.getScheduler().runTask(
                        SkDisaster.getInstance(),
                        () -> {
                            Object[] values = getValues(event, key);
                            if (values == null) {
                                values = new Object[0];
                            }
                            variable.change(event, values, Changer.ChangeMode.SET);
                            TriggerItem.walk(next, event);
                        }
                ),
                out -> writePayload(event, out)
        );

        return null;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        if (!initExpressions(exprs)) {
            return false;
        }

        getParser().setHasDelayBefore(Kleenean.TRUE);
        return true;
    }

    protected boolean initVariable(Expression<?> expr) {
        if (!(expr instanceof Variable)) {
            Skript.error("You must store the result in a list variable.");
            return false;
        }

        variable = (Variable<?>) expr;
        if (!variable.isList()) {
            Skript.error("You must store the result in a list variable.");
            return false;
        }

        return true;
    }
}