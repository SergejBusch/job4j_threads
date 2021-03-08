package ru.job4j.concurrent.threads;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class WgetDownload implements Runnable {
    private final String url;
    private final int speed;

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
            long[] results;
            while ((results = (read(dataBuffer, in)))[0] != -1) {
                fileOutputStream.write(dataBuffer, 0, (int) results[0]);
                long time = calculateTime(results[1], results[2]);
                long sleepTime = getSleepTime(bufferSize, time);
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

    private long[] read(byte[] dataBuffer, BufferedInputStream in) throws IOException {
        int bytesRead;
        long begin = System.currentTimeMillis();
        bytesRead = in.read(dataBuffer, 0, 1024);
        long end = System.currentTimeMillis();
        return new long[] {bytesRead, begin, end};
    }

    private long calculateTime(long begin, long end) {
        return end - begin;
    }

    private long getSleepTime(int numberOfBytes, long time) {
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
