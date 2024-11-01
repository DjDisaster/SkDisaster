package me.djdisaster.testAddon.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CompiledJavaClass;
import me.djdisaster.testAddon.utils.CompiledJavaClassInstance;
import me.djdisaster.testAddon.utils.ReadImage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;

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
        int value;
        if (colourToCheck == 1) {
            value = colour.getSingle(event).getRed();
        } else if (colourToCheck == 2) {
            value = colour.getSingle(event).getBlue();
        } else if (colourToCheck == 3) {
            value = colour.getSingle(event).getGreen();
        } else {
            return null;
        }

        return new Integer[]{value};

    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "compile[d] java [code] from %string% with class name %string%";
    }

}