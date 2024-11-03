package me.djdisaster.testAddon.utils.reactivity.atomic;

public record ValueChangedEvent<T>(T previous, T current) {
}
