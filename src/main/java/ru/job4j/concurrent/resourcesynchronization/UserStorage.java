package ru.job4j.concurrent.resourcesynchronization;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class UserStorage {

    @GuardedBy("this")
    private final Map<Integer, User> users = new HashMap<>();

    public synchronized boolean add(User user) {
        if (contain(user)) {
            return false;
        }
        users.put(user.getId(), user);
        return true;
    }

    public synchronized boolean update(User user) {
        if (!contain(user)) {
            return false;
        }
        users.put(user.getId(), user);
        return true;
    }

    public synchronized boolean delete(User user) {
        if (!contain(user)) {
            return false;
        }
        users.remove(user.getId());
        return true;
    }

    public synchronized void transfer(int fromId, int toId, int amount)
            throws InterruptedException {
        if (!users.containsKey(fromId) && !users.containsKey(toId)) {
            System.out.println("user not exist");
            return;
        }
        var user1 = users.get(fromId);
        if (user1.getAmount() < amount) {
            System.out.println("come next time when you have more money");
            return;
        }
        wait(100); //  for concurrency tests
        var user2 = users.get(toId);
        user1.setAmount(user1.getAmount() - amount);
        wait(100); //  for concurrency tests
        user2.setAmount(user2.getAmount() + amount);
    }

    public synchronized User getUser(int id) {
        if (!users.containsKey(id)) {
            return null;
        }
        return users.get(id);
    }

    private synchronized boolean contain(User user) {
        return users.containsKey(user.getId());
    }
}
