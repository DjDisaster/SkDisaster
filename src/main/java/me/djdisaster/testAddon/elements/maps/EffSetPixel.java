package me.djdisaster.testAddon.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.function.Functions;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.AsyncManager;
import me.djdisaster.testAddon.utils.CompiledJavaClass;
import me.djdisaster.testAddon.utils.CompiledJavaClassInstance;
import me.djdisaster.testAddon.utils.CustomMap;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Kleenean runAsync;

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
        return "";//""Send bungee message to " + players.toString(event, debug) + " with text " + text.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        map.getSingle(event).setPixel(
                x.getSingle(event).intValue(),
                y.getSingle(event).intValue(),

                r.getSingle(event).intValue(),
                g.getSingle(event).intValue(),
                b.getSingle(event).intValue()
        );

    }


}