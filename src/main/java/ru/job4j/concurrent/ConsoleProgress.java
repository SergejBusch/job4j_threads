package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        String[] symbol = new String[] {"\\", "|", "/"};
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print("\rLoading..." + symbol[i++ > 1 ? i = 0 : i] + ".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(1);
        progress.interrupt();
    }
}
