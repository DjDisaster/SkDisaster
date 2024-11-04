package me.djdisaster.skDisaster.utils.reactivity;

import me.djdisaster.skDisaster.utils.collections.LockedWeakList;
import me.djdisaster.skDisaster.utils.reactivity.action.Action;
import me.djdisaster.skDisaster.utils.reactivity.atomic.Atomic;
import me.djdisaster.skDisaster.utils.reactivity.atomic.ValueChangedEvent;

import java.lang.ref.WeakReference;

public interface Binding<T> extends Atomic<T> {
    static <T, V extends Binding<T>> V source(V source, V self) {
        return source != null ? source : self;
    }

    static <T> Binding<T> create(T defaultValue) {
        return new Impl<>(defaultValue);
    }

    Binding<T> bind(Binding<T> other);

    default Binding<T> unbindAll() {
        this.unbindEvents();

        return unbindBindings();
    }

    Binding<T> unbindBindings();

    Binding<T> copy();

    Binding<T> copyTo(Binding<T> target);

    Binding<T> getBoundCopy();

    Binding<T> unbind(Binding<T> other);

    WeakReference<Binding<T>> getWeakReference();

    class Impl<T> extends Abstract<T> {
        protected Impl(T defaultValue) {
            super(defaultValue);
        }

        @Override
        public Binding<T> copy() {
            return new Binding.Impl<>(get());
        }

        @Override
        public Binding<T> getBoundCopy() {
            Binding<T> copy = copy();

            copy.bind(this);

            return copy;
        }
    }

    abstract class Abstract<T> extends Atomic.Impl<T> implements Binding<T> {
        private final LockedWeakList<Binding<T>> references = new LockedWeakList<>();
        private final WeakReference<Binding<T>> weakReference = new WeakReference<>(this);

        protected Abstract(T defaultValue) {
            super(defaultValue);
        }

        @Override
        public Atomic<T> set(T value) {
            updateValue(value, null);

            return this;
        }

        private void updateValue(T value, Binding<T> source) {
            T previous = get();
            this.internalValue.set(value);

            triggerValueChanged(previous, value, source(source, this));

        }

        private void triggerValueChanged(T previous, T value, Binding<T> source) {
            propagate((binding) -> {
                if (binding instanceof Binding.Abstract<T> abstractBinding) {
                    abstractBinding.updateValue(value, source);
                } else binding.set(value);
            }, source);

            callback.accept(new ValueChangedEvent<>(previous, value));
        }

        protected void propagate(Action<Binding<T>> action, Binding<T> source) {
            references.forAliveReferences(true, (reference) -> {
                if (reference.refersTo(source))
                    return;

                Binding<T> binding = reference.get();

                action.accept(binding);
            });
        }

        @Override
        public Binding<T> bind(Binding<T> other) {
            if (references.contains(other.getWeakReference()))
                return this;

            other.copyTo(this);

            reference(other);

            if (!(other instanceof Binding.Abstract<T> abstractBinding)) {
                throw new IllegalStateException("Cannot reference, not referenceable");
            }

            abstractBinding.reference(this);

            return this;
        }

        @Override
        public Binding<T> unbindBindings() {
            references.clear();

            return this;
        }

        @Override
        public abstract Binding<T> copy();

        @Override
        public Binding<T> copyTo(Binding<T> target) {
            target.set(get());

            return target;
        }

        @Override
        public abstract Binding<T> getBoundCopy();

        @Override
        public WeakReference<Binding<T>> getWeakReference() {
            return weakReference;
        }

        @Override
        public Binding<T> unbind(Binding<T> other) {
            if (!references.contains(other.getWeakReference())) {
                throw new IllegalStateException("Was not bound to this.");
            }

            unreference(other);

            if (!(other instanceof Binding.Abstract<T> abstractBinding)) {
                throw new IllegalStateException("Cannot reference, not referenceablez!");
            }

            abstractBinding.unreference(this);

            return this;
        }

        private void reference(Binding<T> other) {
            references.add(other.getWeakReference());
        }

        private void unreference(Binding<T> other) {
            references.remove(other.getWeakReference());
        }
    }
}
