package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class WgetDownload implements Runnable {
    private final String url;
    private final int speed;
    private long time;

    public WgetDownload(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            int bufferSize = 1024;
            byte[] dataBuffer = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = read(dataBuffer, in)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                long sleepTime = getSleepTime(bufferSize);
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private int read(byte[] dataBuffer, BufferedInputStream in) throws IOException {
        int bytesRead;
        long begin = System.currentTimeMillis();
        bytesRead = in.read(dataBuffer, 0, 1024);
        long end = System.currentTimeMillis();
        calculateTime(begin, end);
        return bytesRead;
    }

    private void calculateTime(long begin, long end) {
        time = end - begin;
    }

    private long getSleepTime(int numberOfBytes) {
        if (numberOfBytes / time > speed) {
           return time - (speed / numberOfBytes);
        }
        return 0;
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new WgetDownload(url, speed));
        wget.start();
        wget.join();
    }
}
