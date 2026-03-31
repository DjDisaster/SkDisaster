package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffFetchUUIDOther extends AsyncStoreEffect {

    static {
        Skript.registerEffect(
                EffFetchUUIDOther.class,
                "fetch [the] [(proxy|velocity)] uuid of %string% [and] store it in %object%"
        );
    }

    private Expression<String> player;

    @Override
    protected String getKey(Event event) {
        String name = player.getSingle(event);
        return "UUIDOther-" + name.toLowerCase();
    }

    @Override
    protected void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException {
        out.writeUTF("UUIDOther");
        out.writeUTF(player.getSingle(event));
    }

    @Override
    protected Object getValue(Event event, String key) {
        return VelocityUtils.getString(key);
    }

    @Override
    protected boolean initExpressions(Expression<?>[] exprs) {
        player = (Expression<String>) exprs[0];
        return initVariable(exprs[1]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fetch proxy uuid of player";
    }
}