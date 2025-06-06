package me.djdisaster.skDisaster.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.maps.CustomMap;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;

public class ExprGetPixel extends SimpleExpression<Color> {
    static {
        Skript.registerExpression(
                ExprGetPixel.class, Color.class, ExpressionType.SIMPLE,
                "pixel %number%,[ ]%number% of map %object%"
        );
    }

    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<CustomMap> map;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        x = (Expression<Number>) exprs[0];
        y = (Expression<Number>) exprs[1];
        map = (Expression<CustomMap>) exprs[2];
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
        CustomMap map = this.map.getSingle(event);
        if (map == null) return null;
        int x = this.x.getSingle(event).intValue();
        int y = this.y.getSingle(event).intValue();
        if (y < 0 || x < 0 || x > 127 || y > 127) {
            return new Color[]{null};
        }
        return new Color[]{map.getPixel(x,y)};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "map from " + map.toString(event, b);
    }
}