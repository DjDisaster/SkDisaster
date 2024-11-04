package me.djdisaster.testAddon.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.maps.CustomMap;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffSetAllPixels extends Effect {
    static {
        Skript.registerEffect(EffSetAllPixels.class, "set [(color|colours) of] all pixels of %object% to %number%,%number%,%number%");
    }

    private Expression<CustomMap> map;
    private Expression<Number> r;
    private Expression<Number> g;
    private Expression<Number> b;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.map = (Expression<CustomMap>) expressions[0];
        this.r = (Expression<Number>) expressions[1];
        this.g = (Expression<Number>) expressions[2];
        this.b = (Expression<Number>) expressions[3];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set all pixels of " + map.toString(event, debug) + " to " + r.toString(event, debug) + "," + g.toString(event, debug) + "," + b.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Number r = this.r.getSingle(event);
        Number g = this.g.getSingle(event);
        Number b = this.b.getSingle(event);
        CustomMap map = this.map.getSingle(event);
        if (r == null || g == null || b == null || map == null) return;
        map.setAllPixels(
                r.intValue(),
                g.intValue(),
                b.intValue()
        );
    }
}