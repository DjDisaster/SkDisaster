package me.djdisaster.skDisaster.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.ReadImage;
import org.bukkit.Color;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprPixelOfImage extends SimpleExpression<Color> {
    static {
        Skript.registerExpression(
                ExprPixelOfImage.class, Color.class, ExpressionType.SIMPLE,
                "pixel %number%,[ ]%number% of image %object%"
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
        Number x = this.x.getSingle(event);
        Number y = this.y.getSingle(event);
        ReadImage image = this.image.getSingle(event);
        if (x == null || y == null | image == null) return null;
        return new Color[]{
                image.getPixelAt(x.intValue(), y.intValue())
        };
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "pixel " + x.toString(event, b) + ", " + y.toString(event, b) + " of " + image.toString(event, b);
    }
}