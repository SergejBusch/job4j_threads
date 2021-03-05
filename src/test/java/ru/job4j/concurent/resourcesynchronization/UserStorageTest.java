package ru.job4j.concurent.resourcesynchronization;

import org.junit.Test;
import ru.job4j.concurrent.resourcesynchronization.User;
import ru.job4j.concurrent.resourcesynchronization.UserStorage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserStorageTest {

    @Test
    public void testStorageWithConcurrency() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        UserStorage storage = new UserStorage();
        for (int i = 0; i < numberOfThreads; i++) {
            int id = i;
            service.submit(() -> {
                try {
                    storage.add(new User(id, 0));
                    storage.add(new User(id + numberOfThreads, 100));
                    storage.transfer(id + numberOfThreads, 1, 100);
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        latch.await();
        assertThat(storage.getUser(1).getAmount(), is(1000));
    }

    @Test
    public void testStorageWithoutConcurrency() throws InterruptedException {
        UserStorage storage = new UserStorage();

            storage.add(new User(1, 0));
            storage.add(new User(2, 100));
            storage.transfer(2, 1, 100);

        assertThat(storage.getUser(1).getAmount(), is(100));
    }
}
