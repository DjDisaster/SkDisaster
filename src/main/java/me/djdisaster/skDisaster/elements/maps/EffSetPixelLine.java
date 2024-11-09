package me.djdisaster.skDisaster.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.maps.CustomMap;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

public class EffSetPixelLine extends Effect {
    static {
        Skript.registerEffect(EffSetPixelLine.class, "draw line between %number%,[ ]%number% and %number%,[ ]%number% on %object% (coloured|colored|with) %number%,%number%,%number%[,[ ]%-number%]");
    }

    private Expression<Number> x;
    private Expression<Number> y;
    private Expression<Number> x2;
    private Expression<Number> y2;
    private Expression<CustomMap> map;
    private Expression<Number> r;
    private Expression<Number> g;
    private Expression<Number> b;
    private Expression<Number> alpha;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.x = (Expression<Number>) expressions[0];
        this.y = (Expression<Number>) expressions[1];
        this.x2 = (Expression<Number>) expressions[2];
        this.y2 = (Expression<Number>) expressions[3];
        this.map = (Expression<CustomMap>) expressions[4];
        this.r = (Expression<Number>) expressions[5];
        this.g = (Expression<Number>) expressions[6];
        this.b = (Expression<Number>) expressions[7];
        this.alpha = (Expression<Number>) expressions[8];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "set pixels between " + r.toString(event, debug) + "," + g.toString(event, debug) + "," + b.toString(event, debug) + " from " + x.toString(event, debug) + "," + y.toString(event, debug) + "to" + x2.toString(event, debug) + "," + y2.toString(event, debug) + " of " + map.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        Number x = this.x.getSingle(event);
        Number y = this.y.getSingle(event);
        Number x2 = this.x2.getSingle(event);
        Number y2 = this.y2.getSingle(event);
        Number r = this.r.getSingle(event);
        Number g = this.g.getSingle(event);
        Number b = this.b.getSingle(event);
        CustomMap map = this.map.getSingle(event);

        Number alpha = 255;
        if (this.alpha != null) {
            alpha = this.alpha.getSingle(event);
        }

        if (x == null || y == null || r == null || g == null || b == null || map == null || x2 == null || y2 == null) return;

        map.drawLine(
                x.intValue(),
                y.intValue(),
                x2.intValue(),
                y2.intValue(),
                r.intValue(),
                g.intValue(),
                b.intValue(),
                alpha.intValue()
        );

    }


}