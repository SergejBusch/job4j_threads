package ru.job4j.concurent.cache;

import static org.hamcrest.core.Is.is;
import org.junit.Test;
import ru.job4j.concurrent.cache.Base;
import ru.job4j.concurrent.cache.Cache;

import static org.hamcrest.MatcherAssert.assertThat;

public class CacheTest {

    @Test
    public void whenAddThenTrue() {
        var base = new Base(1, 1);
        var cache = new Cache();
        assertThat(cache.add(base), is(true));
    }

    @Test
    public void whenUpdateThenTrue() {
        var base = new Base(1, 1);
        var cache = new Cache();
        cache.add(base);
        assertThat(cache.update(base), is(true));
    }

    @Test
    public void whenDeleteThenSize0() {
        var base = new Base(1, 1);
        var cache = new Cache();
        cache.add(base);
        assertThat(cache.delete(base), is(true));
    }

}
