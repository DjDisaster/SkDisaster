package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffFetchPlayerCount extends AsyncStoreEffect {

    static {
        Skript.registerEffect(
                EffFetchPlayerCount.class,
                "fetch [the] [(proxy|velocity)] proxy [player] count [on [server] %-string%] [and] store it in %object%"
        );
    }

    private Expression<String> server;

    @Override
    protected String getKey(Event event) {
        String input = server == null ? null : server.getSingle(event);
        String target = input == null || input.isEmpty() ? "ALL" : input;
        return "PlayerCount-" + target;
    }

    @Override
    protected void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException {
        String input = server == null ? null : server.getSingle(event);
        String target = input == null || input.isEmpty() ? "ALL" : input;
        out.writeUTF("PlayerCount");
        out.writeUTF(target);
    }

    @Override
    protected Object getValue(Event event, String key) {
        return VelocityUtils.getInt(key);
    }

    @Override
    protected boolean initExpressions(Expression<?>[] exprs) {
        server = (Expression<String>) exprs[0];
        return initVariable(exprs[1]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fetch proxy player count";
    }
}