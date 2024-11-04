package me.djdisaster.skDisaster.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.maps.CustomMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.map.MapView;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;

public class EffForceMapUpdate extends Effect {
    static {
        Skript.registerEffect(EffForceMapUpdate.class, "update map %object% for %players%");
    }
    private Expression<CustomMap> map;
    private Expression<Player> players;
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.map = (Expression<CustomMap>) expressions[0];
        this.players = (Expression<Player>) expressions[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Force update map: " + map.toString(event, debug) + " for players " + players.toString(event, debug);
    }

    @Override
    protected void execute(Event event) {
        CustomMap map = this.map.getSingle(event);
        if (map == null) {
            return;
        }
        MapView view = map.getView();
        Arrays.stream(players.getArray(event)).forEach(p -> {
            p.sendMap(view);
        });

    }


}