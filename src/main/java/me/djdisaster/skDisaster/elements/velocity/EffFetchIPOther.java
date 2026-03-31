package me.djdisaster.skDisaster.elements.velocity;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import me.djdisaster.skDisaster.utils.velocity.VelocityUtils;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffFetchIPOther extends AsyncStoreEffect {

    static {
        Skript.registerEffect(
                EffFetchIPOther.class,
                "fetch [the] [(proxy|velocity)] ip of %string% [and] store it in %object%"
        );
    }

    private Expression<String> player;

    @Override
    protected String getKey(Event event) {
        return "IPOther-" + player.getSingle(event).toLowerCase();
    }

    @Override
    protected void writePayload(Event event, java.io.DataOutputStream out) throws java.io.IOException {
        out.writeUTF("IPOther");
        out.writeUTF(player.getSingle(event));
    }

    @Override
    protected Object getValue(Event event, String key) {
        VelocityUtils.InetData data = VelocityUtils.getInetData(key);
        if (data == null) {
            return null;
        }
        return data.getIp() + ":" + data.getPort();
    }

    @Override
    protected boolean initExpressions(Expression<?>[] exprs) {
        player = (Expression<String>) exprs[0];
        return initVariable(exprs[1]);
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "fetch proxy ip of player";
    }
}