package me.djdisaster.testAddon.elements.java;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.djdisaster.testAddon.utils.CompiledJavaClass;
import me.djdisaster.testAddon.utils.JavaCompilerManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ExprCompileJavaCode extends SimpleExpression<CompiledJavaClass> {
    static {
        Skript.registerExpression(
                ExprCompileJavaCode.class, CompiledJavaClass.class, ExpressionType.SIMPLE,
                "compile[d] java [code] from %string% with class name %string%"
        );
    }

    private Expression<String> code;
    private Expression<String> className;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        code = (Expression<String>) exprs[0];
        className = (Expression<String>) exprs[1];
        return true;
    }


    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends CompiledJavaClass> getReturnType() {
        return CompiledJavaClass.class;
    }

    @Override
    protected @Nullable CompiledJavaClass[] get(Event event) {


        // public class Test { public static void run() { System.out.println("Hello, world!"); } }
        CompiledJavaClass compiledJavaClass = null;
        try {
            compiledJavaClass = JavaCompilerManager.compile(className.getSingle(event), code.getSingle(event));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new CompiledJavaClass[]{compiledJavaClass};
    }


    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "compile[d] java [code] from %string% with class name %string%";
    }
}