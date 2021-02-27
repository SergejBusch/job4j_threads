package ru.job4j.concurrent;

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
        while (first.getState() != terminated) {
            System.out.println("i'm wait");
        }
        while (second.getState() != terminated) {
            System.out.println("i'm wait too");
        }
        System.out.println("work completed");

    }
}
