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

public class EffAddValueChangeListener extends Effect {

    static {
        Skript.registerEffect(EffAddValueChangeListener.class, "add [new] listener to %object% to run [function] [named] %string% [with id %-string%]");
    }

    private Expression<Object> binding;
    private Expression<String> functionName;
    private Expression<String> id;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.binding = (Expression<Object>) expressions[0];
        this.functionName = (Expression<String>) expressions[1];
        this.id = (Expression<String>) expressions[2];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "add listener: " + binding.getSingle(event);
    }

    @Override
    protected void execute(Event event) {

        if (this.binding.getSingle(event) == null) {
            return;
        }

        Binding binding = (Binding) this.binding.getSingle(event);
        if (binding == null) {
            Bukkit.getLogger().warning("EffAddValueChangeListener called with null. Trying to add: " + functionName.getSingle(event));
            return;
        }
        String functionName = this.functionName.getSingle(event);
        String id;
        if (this.id == null) {
            id = "";
        } else {
            id = this.id.getSingle(event);
        }

        binding.addListener(new BindingSkriptListener(
                functionName,
                id
        ));
    }


}