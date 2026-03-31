package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffMessagePlayer extends Effect {

    static {
        Skript.registerEffect(
                EffMessagePlayer.class,
                "send [proxy] message %string% to %string%"
        );
    }

    private Expression<String> message;
    private Expression<String> player;

    @Override
    protected void execute(Event event) {
        String text = message.getSingle(event);
        String name = player.getSingle(event);
        if (text != null && name != null) {
            VelocityUtils.message(name, text);
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        message = (Expression<String>) exprs[0];
        player = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "send proxy message";
    }
}