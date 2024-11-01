package me.djdisaster.testAddon.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;

public class ExprLinesOfFile extends SimpleExpression<String> {
    static {
        Skript.registerExpression(
                ExprLinesOfFile.class, String.class, ExpressionType.SIMPLE,
                "lines of file %string%"
        );
    }

    private Expression<String> fileName;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        fileName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @Nullable String[] get(Event event) {
        String fileName = this.fileName.getSingle(event);
        if (fileName == null) return null;
        File file = new File(fileName);
        if (!file.exists()) {
            Bukkit.getLogger().warning("Tried to read from file: " + file.getPath() + " but it doesn't exist!");
            return null;
        }
        try {
            return Files.readAllLines(file.toPath()).toArray(new String[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "lines of file " + fileName.toString(event, b);
    }
}