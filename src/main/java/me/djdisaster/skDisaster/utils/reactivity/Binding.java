package me.djdisaster.skDisaster.utils.reactivity;

import java.util.ArrayList;
import java.util.List;

public class Binding {

    public Object value;

    private List<BindingSkriptListener> listeners = new ArrayList<>();

    public Binding(Object value) {
        this.value = value;
    }

    public void setValue(Object newValue) {
        Object oldValue = value;
        value = newValue;
        listeners.forEach(l -> l.invoke(oldValue, value, this));
    }

    public Object getValue() {
        return value;
    }

    public void addListener(BindingSkriptListener listener) {
        listeners.add(listener);
    }

    public List<BindingSkriptListener> getListeners() {
        return listeners;
    }

}
