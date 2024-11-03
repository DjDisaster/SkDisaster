package me.djdisaster.testAddon.utils.reactivity;

import me.djdisaster.testAddon.utils.reactivity.atomic.Atomic;

public class Main {
    public static void main(String[] args) {
        Binding<Float> binding = Binding.create(100.0f);
        Binding<Float> bound = binding.getBoundCopy();

        binding.onValueChangedEvent((event) -> System.out.println(event.previous()));


        Atomic<Float> atomic = Atomic.create(100.0f);

        atomic.onValueChanged(System.out::println);

        atomic.set(200.0f);
    }
}
