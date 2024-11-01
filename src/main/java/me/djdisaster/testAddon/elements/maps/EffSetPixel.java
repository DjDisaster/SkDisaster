package me.djdisaster.testAddon.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CustomMap;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffSetPixel extends Effect {
    static {
        Skript.registerEffect(EffSetPixel.class, "set pixel [color|colour] %number%,[ ]%number% of %object% to %number%,%number%,%number%");
    }

    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<CustomMap> map;
    private Expression<Number> r;
    private Expression<Number> g;
    private Expression<Number> b;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.x = (Expression<Number>) expressions[0];
        this.y = (Expression<Number>) expressions[1];
        this.map = (Expression<CustomMap>) expressions[2];
        this.r = (Expression<Number>) expressions[3];
        this.g = (Expression<Number>) expressions[4];
        this.b = (Expression<Number>) expressions[5];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set pixel color " + r.toString(event, debug) + "," + g.toString(event, debug) + "," + b.toString(event, debug) + " at " + x.toString(event, debug) + "," + y.toString(event, debug) + " of " + map.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Number x = this.x.getSingle(event);
        Number y = this.y.getSingle(event);
        Number r = this.r.getSingle(event);
        Number g = this.g.getSingle(event);
        Number b = this.b.getSingle(event);
        CustomMap map = this.map.getSingle(event);
        if (x == null || y == null || r == null || g == null || b == null || map == null) return;

        map.setPixel(
                x.intValue(),
                y.intValue(),
                r.intValue(),
                g.intValue(),
                b.intValue()
        );

    }


}