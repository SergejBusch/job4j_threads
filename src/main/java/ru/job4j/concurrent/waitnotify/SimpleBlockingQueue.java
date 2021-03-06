package ru.job4j.concurrent.waitnotify;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    private final int limit;

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int limit) {
        this.limit = limit;
    }

    public boolean offer(T value) {
        synchronized (this) {
            while (queue.size() == limit) {
                waitForThis();
            }
            var result = queue.add(value);
            this.notifyAll();
            return result;
        }
    }

    public T poll() {
        synchronized (this) {
            while (queue.size() < 1) {
                waitForThis();
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            T value = queue.poll();
            this.notifyAll();
            if (value == null) {
                throw new NoSuchElementException();
            }
            return value;
        }
    }

    private void waitForThis() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
