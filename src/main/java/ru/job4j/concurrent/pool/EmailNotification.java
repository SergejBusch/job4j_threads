package ru.job4j.concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public String[] emailTo(User user) {
        String subject = String.format("Notification %s to email %s",
                user.getName(), user.getEmail());
        String body = String.format("Add a new event to %s",
                user.getName());
        return new String[]{subject, body, user.getEmail()};
    }

    public void send(String subject, String body, String email) {
    }

    public void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        var emailNotification = new EmailNotification();
        emailNotification.pool.submit(() -> {
            System.out.println("Execute " + Thread.currentThread().getName());
            var user = new User("Tom", "tom@mail.com");
            var data = emailNotification.emailTo(user);
            emailNotification.send(data[0], data[1], data[2]);
        });
        emailNotification.close();
    }
}
