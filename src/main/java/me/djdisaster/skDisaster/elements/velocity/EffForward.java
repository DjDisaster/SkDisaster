package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class EffForward extends Effect {

    static {
        Skript.registerEffect(
                EffForward.class,
                "forward [proxy] payload %string% to [server] %string% on [channel] %string%"
        );
    }

    private Expression<String> payload;
    private Expression<String> server;
    private Expression<String> channel;

    @Override
    protected void execute(Event event) {
        String payloadValue = payload.getSingle(event);
        String serverValue = server.getSingle(event);
        String channelValue = channel.getSingle(event);

        if (payloadValue == null || serverValue == null || channelValue == null) {
            return;
        }

        VelocityUtils.forward(serverValue, channelValue, payloadValue.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        payload = (Expression<String>) exprs[0];
        server = (Expression<String>) exprs[1];
        channel = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "forward proxy payload";
    }
}