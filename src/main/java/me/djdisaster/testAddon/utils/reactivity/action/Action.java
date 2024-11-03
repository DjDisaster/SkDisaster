package me.djdisaster.testAddon.utils.reactivity.action;

@FunctionalInterface
public interface Action<T> {
    void accept(T value);
}
