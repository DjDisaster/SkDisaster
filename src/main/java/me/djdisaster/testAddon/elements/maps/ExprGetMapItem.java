package me.djdisaster.testAddon.elements.maps;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CompiledJavaClass;
import me.djdisaster.testAddon.utils.CompiledJavaClassInstance;
import me.djdisaster.testAddon.utils.CustomMap;
import me.djdisaster.testAddon.utils.MapData;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;

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
        return new ItemStack[]{map.getSingle(event).getItem()};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "map from object";
    }

}