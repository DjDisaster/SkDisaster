package me.djdisaster.skDisaster.utils.reactivity.action;

import me.djdisaster.skDisaster.utils.reactivity.atomic.ValueChangedEvent;

import java.util.ArrayList;
import java.util.List;

public interface Callback<T> extends Action<T> {
    static <T> Callback<T> create() {
        return new Impl<>();
    }

    Callback<T> subscribe(Action<T> action);

    Callback<T> unsubscribe(Action<T> action);

    boolean isSubscribed(Action<T> action);

    Callback<T> clear();

    class Impl<T> implements Callback<T> {
        private final List<Action<T>> subscribers = new ArrayList<>();

        @SafeVarargs
        public Impl(Action<T>... subscribers) {
            for (Action<T> subscriber : subscribers) {
                subscribe(subscriber);
            }
        }

        @Override
        public Callback<T> subscribe(Action<T> action) {
            if (action == this) {
                throw new IllegalStateException("Cannot subscribe to ourselves?");
            }

            synchronized (subscribers) {
                subscribers.add(action);
            }

            return this;
        }

        @Override
        public Callback<T> unsubscribe(Action<T> action) {
            synchronized (subscribers) {
                subscribers.remove(action);
            }

            return this;
        }

        @Override
        public boolean isSubscribed(Action<T> action) {
            return subscribers.contains(action);
        }

        @Override
        public Callback<T> clear() {
            synchronized (subscribers) {
                subscribers.clear();
            }

            return this;
        }

        @Override
        public void accept(T value) {
            synchronized (subscribers) {
                subscribers.forEach((subscriber) -> subscriber.accept(value));
            }
        }
    }

    class Valued<T> extends Impl<ValueChangedEvent<T>> {

        @SafeVarargs
        public Valued(Action<ValueChangedEvent<T>>... subscribers) {
            super(subscribers);
        }
    }
}
