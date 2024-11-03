package me.djdisaster.testAddon.utils.reactivity.atomic;

import me.djdisaster.testAddon.utils.reactivity.Binding;
import me.djdisaster.testAddon.utils.reactivity.action.Action;
import me.djdisaster.testAddon.utils.reactivity.action.Callback;

import java.util.concurrent.atomic.AtomicReference;

public interface ImmutableAtomic<T> {
    T get();

    default ImmutableAtomic<T> onValueChanged(Action<T> action, boolean runOnceImmediately) {
        return onValueChangedEvent((event) -> action.accept(event.current()), runOnceImmediately);
    }

    default ImmutableAtomic<T> onValueChanged(Action<T> action) {
        return onValueChanged(action, false);
    }

    ImmutableAtomic<T> onValueChangedEvent(Action<ValueChangedEvent<T>> action, boolean runOnceImmediately);

    default ImmutableAtomic<T> onValueChangedEvent(Action<ValueChangedEvent<T>> action) {
        return onValueChangedEvent(action, false);
    }

    ImmutableAtomic<T> unbindEvents();

    abstract class Abstract<T> implements ImmutableAtomic<T> {
        protected final AtomicReference<T> internalValue;
        protected final Callback.Valued<T> callback = new Callback.Valued<>();

        protected Abstract(final T defaultValue) {
            this.internalValue = new AtomicReference<>(defaultValue);
        }

        @Override
        public T get() {
            return internalValue.get();
        }

        @Override
        public ImmutableAtomic<T> onValueChangedEvent(Action<ValueChangedEvent<T>> action, boolean runOnceImmediately) {
            callback.subscribe(action);

            if (runOnceImmediately) {
                action.accept(new ValueChangedEvent<>(get(), get()));
            }

            return this;
        }

        @Override
        public ImmutableAtomic<T> unbindEvents() {
            callback.clear();

            return this;
        }
    }
}
