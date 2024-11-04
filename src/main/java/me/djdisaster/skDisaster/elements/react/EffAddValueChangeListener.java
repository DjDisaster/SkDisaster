package me.djdisaster.skDisaster.elements.react;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.function.Functions;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.reactivity.Binding;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffAddValueChangeListener extends Effect {

    static {
        Skript.registerEffect(EffAddValueChangeListener.class, "add [new] listener to %object% to run [function] [named] %string%");
    }

    private Expression<Object> binding;
    private Expression<String> functionName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.binding = (Expression<Object>) expressions[0];
        this.functionName = (Expression<String>) expressions[1];

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
        if (!(this.binding.getSingle(event) instanceof Binding.Impl<?>)) {
            return;
        }

        Binding<Object> binding = (Binding<Object>) this.binding.getSingle(event);
        String functionName = this.functionName.getSingle(event);

        binding.onValueChangedEvent((e) -> {
            Functions.getGlobalFunction(functionName).execute(new Object[][]{
                    new Object[]{e},
                    new Object[]{e.previous()},
                    new Object[]{e.current()}

            });
        });




    }


}