package ru.job4j.concurrent;

import java.util.Random;

public class Wget {
    public static void main(String[] args) {

        Thread thread = new Thread(
                () -> {
                    try {
                        for (int i = 0; i < 101; i++) {
                            System.out.print("\rLoading " + i + "%");
                            Random random = new Random();
                            int randomInt = random.nextInt(1000);
                            Thread.sleep(randomInt);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        thread.start();
    }
}
