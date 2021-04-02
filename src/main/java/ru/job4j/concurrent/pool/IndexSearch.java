package ru.job4j.concurrent.pool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class IndexSearch<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final T element;
    private final int lowIndex;
    private final int highIndex;

    public IndexSearch(T[] array, int lowIndex, int highIndex, T element) {
        this.array = array;
        this.lowIndex = lowIndex;
        this.highIndex = highIndex;
        this.element = element;
    }

    @Override
    protected Integer compute() {
        if (highIndex - lowIndex > 11) {

            int middleIndex = (lowIndex + highIndex) / 2;

            IndexSearch<T> search1 = new IndexSearch<>(array, lowIndex, middleIndex, element);
            IndexSearch<T> search2 = new IndexSearch<>(array, middleIndex + 1, highIndex, element);

            invokeAll(search1, search2);

            Integer result1 = search1.join();
            Integer result2 = search2.join();

            return result1 != null ? result1 : result2;

        } else {
            return sequentialSearch(array);
        }
    }

    private Integer sequentialSearch(T[] elements) {
        for (int i = 0; i < elements.length - 1; i++) {
            if (elements[i] == element) {
                return i;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        Random random = new Random();
        Integer[] array = new Integer[2000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }
        IndexSearch<Integer> indexSearch = new IndexSearch<>(array, 0, array.length - 1, 5);
        System.out.println(Arrays.toString(array));
        System.out.println(pool.invoke(indexSearch));
    }
}
