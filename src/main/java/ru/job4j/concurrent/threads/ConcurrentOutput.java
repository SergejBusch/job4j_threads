package ru.job4j.concurrent.threads;

public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        first.start();
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        second.start();
        var terminated = Thread.State.TERMINATED;
        while (state(first) || state(second)) {
            System.out.println("i'm wait");
        }
        System.out.println(first.getState() + " " + second.getState() + " work completed");
    }

    private static boolean state(Thread s) {
        System.out.println(s.getName() + " " + s.getState());
        return s.getState() != Thread.State.TERMINATED;

    }
}
