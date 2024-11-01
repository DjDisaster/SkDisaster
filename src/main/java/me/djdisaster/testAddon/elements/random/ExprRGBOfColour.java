package me.djdisaster.testAddon.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprRGBOfColour extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(
                ExprRGBOfColour.class, Integer.class, ExpressionType.SIMPLE,
                "(1¦red|2¦blue|3¦green) of %object%"
        );
    }

    private Expression<Color> colour;
    private int colourToCheck;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        colour = (Expression<Color>) exprs[0];
        colourToCheck = parseResult.mark;
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    protected @Nullable Integer[] get(Event event) {
        Color colour = this.colour.getSingle(event);
        if (colour == null) return null;

        int value = switch (colourToCheck) {
            case 1 -> colour.getRed();
            case 2 -> colour.getBlue();
            case 3 -> colour.getGreen();
            default -> 0;
        };

        return new Integer[]{value};

    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return switch (colourToCheck) {
            case 1 -> "red of " + colour.toString(event, b);
            case 2 -> "blue of " + colour.toString(event, b);
            case 3 -> "green of " + colour.toString(event, b);
            default -> "";
        };
    }
}