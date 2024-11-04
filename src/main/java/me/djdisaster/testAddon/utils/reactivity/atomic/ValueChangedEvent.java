package me.djdisaster.testAddon.utils.reactivity.atomic;

import me.djdisaster.testAddon.utils.reactivity.Binding;

public record ValueChangedEvent<T>(T previous, T current) {
}
