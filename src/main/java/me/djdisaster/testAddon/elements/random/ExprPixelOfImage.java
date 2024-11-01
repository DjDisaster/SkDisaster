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

public class ExprPixelOfImage extends SimpleExpression<Color> {
    static {
        Skript.registerExpression(
                ExprPixelOfImage.class, Color.class, ExpressionType.SIMPLE,
                "pixel %number%,[ ]%number% of %object%"
        );
    }

    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<ReadImage> image;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        x = (Expression<Number>) exprs[0];
        y = (Expression<Number>) exprs[1];
        image = (Expression<ReadImage>) exprs[2];
        return true;
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Color> getReturnType() {
        return Color.class;
    }

    @Override
    protected @Nullable Color[] get(Event event) {
        return new Color[]{
                image.getSingle(event).getPixelAt(x.getSingle(event).intValue(),y.getSingle(event).intValue())
        };
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "compile[d] java [code] from %string% with class name %string%";
    }

}