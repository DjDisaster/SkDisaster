package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffKickPlayer extends Effect {

    static {
        Skript.registerEffect(
                EffKickPlayer.class,
                "kick [proxy] player %string% [(for|because [of])] %string%"
        );
    }

    private Expression<String> player;
    private Expression<String> reason;

    @Override
    protected void execute(Event event) {
        String name = player.getSingle(event);
        String text = reason.getSingle(event);
        if (name != null && text != null) {
            VelocityUtils.kickPlayer(name, text);
        }
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        player = (Expression<String>) exprs[0];
        reason = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "kick proxy player";
    }
}