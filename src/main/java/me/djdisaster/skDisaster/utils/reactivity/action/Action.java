package me.djdisaster.skDisaster.utils.reactivity.action;

@FunctionalInterface
public interface Action<T> {
    void accept(T value);
}
