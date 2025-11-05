package me.djdisaster.skDisaster.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.function.Functions;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.reactivity.Binding;
import me.djdisaster.skDisaster.utils.reactivity.BindingSkriptListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffRemoveChangeListener extends Effect {

    static {
        Skript.registerEffect(EffRemoveChangeListener.class, "remove listener %object% from %object%");
    }

    private Expression<Object> listener;
    private Expression<Object> binding;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.listener = (Expression<Object>) expressions[0];
        this.binding = (Expression<Object>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "remove listener: " + listener.getSingle(event);
    }

    @Override
    protected void execute(Event event) {

        if (this.binding.getSingle(event) == null) {
            return;
        }

        Binding binding = (Binding) this.binding.getSingle(event);
        if (binding == null) {
            Bukkit.getLogger().warning("EffRemoveChangeListener called with null binding.");
            return;
        }

        BindingSkriptListener listener = (BindingSkriptListener) this.listener.getSingle(event);
        if (listener == null) {
            Bukkit.getLogger().warning("EffRemoveChangeListener called with null listener.");
            return;
        }

        binding.getListeners().remove(listener);
    }


}