package me.djdisaster.testAddon.utils.collections;

import me.djdisaster.testAddon.utils.reactivity.action.Action;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LockedWeakList<T> {
    private final Object lock = new Object();
    private final List<WeakReference<T>> references = new ArrayList<>();

    public boolean add(WeakReference<T> reference) {
        return references.add(reference);
    }

    public boolean contains(WeakReference<T> reference) {
        return references.contains(reference);
    }

    public boolean remove(WeakReference<T> reference) {
        return references.remove(reference);
    }

    public void clear() {
        references.clear();
    }

    public LockedWeakList<T> forAliveReferences(boolean cleanUp, Action<WeakReference<T>> action) {
        List<WeakReference<T>> aliveReferences = getAliveReferences(cleanUp);

        aliveReferences.forEach(action::accept);

        return this;
    }

    public List<WeakReference<T>> getAliveReferences(boolean cleanUp) {
        synchronized (lock) {
            if (cleanUp) {
                references.removeIf((element) -> element.get() == null);
            }

            return references;
        }
    }

    public Iterator<WeakReference<T>> iterator() {
        return references.iterator();
    }
}