package me.djdisaster.skDisaster.utils.reactivity;

import ch.njol.skript.lang.function.Functions;

public class BindingSkriptListener {

    private String functionName;
    private String id;

    public BindingSkriptListener(String functionName, String id) {
        this.functionName = functionName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void invoke(Object oldValue, Object newValue, Binding binding) {
        Functions.getGlobalFunction(functionName).execute(new Object[][]{
                new Object[]{id},
                new Object[]{oldValue},
                new Object[]{newValue},
                new Object[]{binding}
        });
    }

}
