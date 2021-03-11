package ru.job4j.concurrent.sync;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
        var clazz = simpleArray.get(0).getClass();
        TypeToken typeToken = TypeToken.get(clazz);
        var gson = new Gson();
        var cloneList = new SimpleArray<T>();

        for (var e : simpleArray) {
            cloneList.add(gson.fromJson(gson.toJson(e), typeToken.getType()));

        }
        return cloneList;
    }
}


