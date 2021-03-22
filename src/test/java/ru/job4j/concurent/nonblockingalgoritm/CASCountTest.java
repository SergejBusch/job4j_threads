package ru.job4j.concurent.nonblockingalgoritm;

import org.junit.Test;
import ru.job4j.concurrent.nonblockingalgoritm.CASCount;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

public class CASCountTest {

    @Test
    public void multiThreadingTest() {
        var counter = new CASCount();
        System.out.println(counter.get());

        var thread1 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        });
        thread1.start();

        var thread2 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        });
        thread2.start();

        var thread3 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            for (int i = 0; i < 1_000_000; i++) {
                counter.increment();
            }
        });
        thread3.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(counter.get(), greaterThan(1_000_000));
        assertThat(counter.get(), lessThan(3_000_000));

        try {

            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(counter.get(), is(3_000_000));
    }
}
