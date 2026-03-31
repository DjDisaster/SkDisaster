package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffFetchCurrentServer extends AsyncStoreEffect {

    static {
        Skript.registerEffect(
                EffFetchCurrentServer.class,
                "fetch [the] [(proxy|velocity)] current server [and] store it in %object%"
        );
    }

    @Override
    protected String getKey(Event event) {
        return "GetServer";
    }

    @Override
    protected void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException {
        out.writeUTF("GetServer");
    }

    @Override
    protected Object getValue(Event event, String key) {
        return VelocityUtils.getString(key);
    }

    @Override
    protected boolean initExpressions(Expression<?>[] exprs) {
        return initVariable(exprs[0]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fetch current server";
    }
}