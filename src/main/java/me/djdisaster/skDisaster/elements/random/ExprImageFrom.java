package me.djdisaster.skDisaster.elements.random;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.skDisaster.utils.ReadImage;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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
        isURL = parseResult.mark != 1;
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
        return "image from " + filePath.toString(event, b);
    }
}