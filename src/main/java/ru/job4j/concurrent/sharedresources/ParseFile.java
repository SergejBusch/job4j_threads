package ru.job4j.concurrent.sharedresources;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContent() throws IOException {
        Path path = file.toPath();
        return String.join("\n", Files.readAllLines(path));
    }

    public synchronized String getContentWithoutUnicode() throws IOException {
        Path path = file.toPath();
        return String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));

    }

    public synchronized void saveContent(String content) {
        try (OutputStream o = new FileOutputStream(file)) {
            byte[] strToBytes = content.getBytes();
            o.write(strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized File getFile() {
        return file;
    }
}
