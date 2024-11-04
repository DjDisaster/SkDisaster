package me.djdisaster.skDisaster.utils.reactivity.action;

public class Subscription<T> implements AutoCloseable {
    private final Callback<T> callback;
    private final Action<T> subscriber;

    public Subscription(final Callback<T> callback, final Action<T> action) {
        this.callback = callback;
        this.subscriber = action;
    }

    public Subscription<T> reify() {
        if (callback.isSubscribed(subscriber)) {
            throw new IllegalStateException("Subscriber was already subscribed to the callback");
        }

        return new Subscription<>(callback, subscriber);
    }

    @Override
    public void close() throws Exception {
        if (!callback.isSubscribed(subscriber)) {
            throw new IllegalStateException("Subscriber was not subscribed to the callback");
        }

        callback.unsubscribe(subscriber);
    }
}
