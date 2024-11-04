package me.djdisaster.skDisaster.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.maps.CustomMap;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprNewMap extends SimpleExpression<CustomMap> {
    static {
        Skript.registerExpression(
                ExprNewMap.class, CustomMap.class, ExpressionType.SIMPLE,
                "new custom map"
        );
    }

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends CustomMap> getReturnType() {
        return CustomMap.class;
    }

    @Override
    protected @Nullable CustomMap[] get(Event event) {
        return new CustomMap[]{new CustomMap(false)};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "new custom map";
    }
}