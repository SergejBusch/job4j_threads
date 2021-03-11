package ru.job4j.concurrent.sync;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Iterator;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {

    @GuardedBy("this")
    final private SimpleArray<T> simpleArray = new SimpleArray<>();

    public synchronized void add(T value) {
        simpleArray.add(value);
    }

    public synchronized T get(int index) {
        return simpleArray.get(index);
    }

    @Override
    public Iterator<T> iterator() {
        return copy().iterator();
    }

    private synchronized SimpleArray<T> copy() {
        var cloneList = new SimpleArray<T>();

        for (var e : simpleArray) {
            cloneList.add(e);
        }
        return cloneList;
    }
}


