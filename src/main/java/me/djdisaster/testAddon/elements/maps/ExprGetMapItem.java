package me.djdisaster.testAddon.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.maps.CustomMap;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprGetMapItem extends SimpleExpression<ItemStack> {
    static {
        Skript.registerExpression(
                ExprGetMapItem.class, ItemStack.class, ExpressionType.SIMPLE,
                "map of %object%"
        );
    }

    private Expression<CustomMap> map;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        map = (Expression<CustomMap>) exprs[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    protected @Nullable ItemStack[] get(Event event) {
        CustomMap map = this.map.getSingle(event);
        if (map == null) return null;
        return new ItemStack[]{map.getItem()};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "map from " + map.toString(event, b);
    }
}