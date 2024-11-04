package me.djdisaster.skDisaster.utils.reactivity.atomic;

public record ValueChangedEvent<T>(T previous, T current) {
}
