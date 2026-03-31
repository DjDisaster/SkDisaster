package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffFetchPlayerList extends AsyncListStoreEffect {

    static {
        Skript.registerEffect(
                EffFetchPlayerList.class,
                "fetch [the] [(proxy|velocity)] proxy player list [on [server] %-string%] [and] store it in %objects%"
        );
    }

    private Expression<String> server;

    @Override
    protected String getKey(Event event) {
        String input = server == null ? null : server.getSingle(event);
        String target = input == null || input.isEmpty() ? "ALL" : input;
        return "PlayerList-" + target;
    }

    @Override
    protected void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException {
        String input = server == null ? null : server.getSingle(event);
        String target = input == null || input.isEmpty() ? "ALL" : input;
        out.writeUTF("PlayerList");
        out.writeUTF(target);
    }

    @Override
    protected Object[] getValues(Event event, String key) {
        String[] values = VelocityUtils.getStringArray(key);
        return values == null ? new Object[0] : values;
    }

    @Override
    protected boolean initExpressions(Expression<?>[] exprs) {
        server = (Expression<String>) exprs[0];
        return initVariable(exprs[1]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fetch proxy player list";
    }
}