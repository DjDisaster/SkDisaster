package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffFetchServers extends AsyncListStoreEffect {

    static {
        Skript.registerEffect(
                EffFetchServers.class,
                "fetch [the] [(proxy|velocity)] proxy servers [and] store [them] in %objects%"
        );
    }

    @Override
    protected String getKey(Event event) {
        return "GetServers";
    }

    @Override
    protected void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException {
        out.writeUTF("GetServers");
    }

    @Override
    protected Object[] getValues(Event event, String key) {
        String[] values = VelocityUtils.getStringArray(key);
        return values == null ? new Object[0] : values;
    }

    @Override
    protected boolean initExpressions(Expression<?>[] exprs) {
        return initVariable(exprs[0]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fetch proxy servers";
    }
}