package me.djdisaster.testAddon.utils.reactivity.atomic;

public interface Atomic<T> extends ImmutableAtomic<T> {
    static <T> Atomic<T> create(T defaultValue) {
        return new Impl<>(defaultValue);
    }

    Atomic<T> set(T value);

    class Impl<T> extends ImmutableAtomic.Abstract<T> implements Atomic<T> {
        public Impl(T defaultValue) {
            super(defaultValue);
        }

        @Override
        public Atomic<T> set(T value) {
            if (get().equals(value))
                return this;

            T previous = get();
            this.internalValue.set(value);

            this.callback.accept(new ValueChangedEvent<>(previous, value));

            return this;
        }
    }
}
