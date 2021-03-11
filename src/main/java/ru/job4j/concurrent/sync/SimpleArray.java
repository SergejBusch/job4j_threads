package ru.job4j.concurrent.sync;

import java.io.Serializable;
import java.util.*;

public class SimpleArray<T> implements Iterable<T> {
    private Object[] simpleArray = new Object[10];
    private int index = 0;
    private int modifications = 0;

    public T get(int index) {
        Objects.checkIndex(index, this.index);
        return (T) simpleArray[index];
    }

    public void add(T model) {
        grow();
        modifications++;
        simpleArray[index++] = model;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private final int modification = modifications;
            private int cursor = 0;
            @Override
            public boolean hasNext() {
                isModified();
                return cursor < index;
            }

            @Override
            public T next() {
                isModified();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) simpleArray[cursor++];
            }

            private void isModified() {
                if (modification != modifications) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    private void grow() {
        if (index == simpleArray.length) {
            simpleArray = Arrays.copyOf(simpleArray, simpleArray.length * 2);
        }
    }
}