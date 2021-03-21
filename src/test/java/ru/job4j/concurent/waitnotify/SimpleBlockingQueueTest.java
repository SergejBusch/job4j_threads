package ru.job4j.concurent.waitnotify;


import org.junit.Test;
import ru.job4j.concurrent.waitnotify.SimpleBlockingQueue;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SimpleBlockingQueueTest {



    @Test
    public void whenPollAndQueueIsEmpty() {
        var list = new ArrayList<Integer>();
        var queue = new SimpleBlockingQueue<Integer>(3);

        var consumer = new Thread(
                () -> list.remove(queue.poll()),
                "Consumer"
        );

        try {
            consumer.start();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThat(list.size(), is(0));

    }

    @Test
    public void whenOfferFourTimeThenListSizeIs3() {
        var list = new ArrayList<Integer>();
        var queue = new SimpleBlockingQueue<Integer>(3);

        var producer = new Thread(
                () -> {
                    queue.offer(1);
                    list.add(1);
                    queue.offer(1);
                    list.add(1);
                    queue.offer(1);
                    list.add(1);
                    queue.offer(1);
                    list.add(1);
                },
                "Producer"
        );

        try {
            producer.start();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThat(list.size(), is(3));

    }

    @Test
    public void whenOfferThreeAndPollThree() {
        var list = new ArrayList<Integer>();
        var queue = new SimpleBlockingQueue<Integer>(3);

        var producer = new Thread(
                () -> {
                    queue.offer(1);
                    list.add(1);
                    queue.offer(1);
                    list.add(1);
                    queue.offer(1);
                    list.add(1);
                },
                "Producer"
        );

        var consumer = new Thread(
                () -> {
                    list.remove(queue.poll());
                    list.remove(queue.poll());
                    list.remove(queue.poll());
                },
                "Consumer"
        );

        try {
            producer.start();
            consumer.start();
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(list.size(), is(0));

    }
}
