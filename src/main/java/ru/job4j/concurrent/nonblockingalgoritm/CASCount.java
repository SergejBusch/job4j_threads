package ru.job4j.concurrent.nonblockingalgoritm;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    public CASCount() {
        count.set(0);
    }

    public void increment() {
        Integer number;
        int incremented;
        do {
            number = count.get();
            incremented = number + 1;
        } while (!count.compareAndSet(number, incremented));
    }

    public int get() {
        return count.get();
    }
}
