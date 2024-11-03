package me.djdisaster.testAddon.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CustomMap;
import me.djdisaster.testAddon.utils.reactivity.Binding;
import me.djdisaster.testAddon.utils.reactivity.atomic.ValueChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffSetValue extends Effect {

    static {
        Skript.registerEffect(EffSetValue.class, "set (Reactive|Atomic|Binding|Value) of %object% to %object%");
    }

    private Expression<Object> binding;
    private Expression<Object> newValue;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.binding = (Expression<Object>) expressions[0];
        this.newValue = (Expression<Object>) expressions[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Change binding: " + binding.getSingle(event) + " to: " + newValue.getSingle(event);
    }

    @Override
    protected void execute(Event event) {

        if (this.binding.getSingle(event) == null) {
            Bukkit.broadcastMessage("null");
            return;
        }
        Object bindingObject = this.binding.getSingle(event);
        if (bindingObject instanceof Binding.Impl<?>) {
            Binding<Object> bindingSingle = (Binding<Object>) this.binding.getSingle(event);
            Object newValue = this.newValue.getSingle(event);
            bindingSingle.set(newValue);
            return;
        } else if (bindingObject instanceof ValueChangedEvent<?>) {
            ValueChangedEvent<?> valueChangedEvent = (ValueChangedEvent<?>) bindingObject;

        }



    }


}