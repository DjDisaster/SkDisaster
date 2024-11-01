package me.djdisaster.testAddon.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CompiledJavaClass;
import me.djdisaster.testAddon.utils.CompiledJavaClassInstance;
import me.djdisaster.testAddon.utils.ReadImage;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;

public class ExprImageFrom extends SimpleExpression<ReadImage> {
    static {
        Skript.registerExpression(
                ExprImageFrom.class, ReadImage.class, ExpressionType.SIMPLE,
                "image from (1¦file|2¦URL) %string%"
        );
    }


    private Expression<String> filePath;
    private boolean isURL;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        if (parseResult.mark == 1) {
            isURL = false;
        } else {
            isURL = true;
        }
        return true;
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ReadImage> getReturnType() {
        return ReadImage.class;
    }

    @Override
    protected @Nullable ReadImage[] get(Event event) {
        return new ReadImage[]{new ReadImage(filePath.getSingle(event), isURL)};
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "image from file";
    }

}