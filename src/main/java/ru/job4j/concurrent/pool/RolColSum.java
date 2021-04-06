package ru.job4j.concurrent.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    public static class Sums {
        private int rowSum;
        private int colSum;

        public int getRowSum() {
            return rowSum;
        }

        public int getColSum() {
            return colSum;
        }
    }

    public enum Type {
        Column,
        Row
    }

    public static Sums[] sum(int[][] matrix) {

        var sums = new Sums[matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            sums[i] = new Sums();
            for (int j = 0; j < matrix[i].length; j++) {
                sums[i].rowSum += matrix[i][j];
                sums[i].colSum += matrix[j][i];
            }
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        var sums = new Sums[matrix.length];
        List<CompletableFuture<Integer>> columns = new ArrayList<>();
        List<CompletableFuture<Integer>> rows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            rows.add(getSum(matrix, i, Type.Column));
            columns.add(getSum(matrix, i, Type.Row));
        }

        for (int i = 0; i < matrix.length; i++) {
            sums[i] = new Sums();
            sums[i].rowSum = rows.get(i).get();
            sums[i].colSum = columns.get(i).get();
        }
        return sums;
    }

    private static CompletableFuture<Integer> getSum(
            int[][] data, int index, Type type) {
        return CompletableFuture.supplyAsync(() -> {
            int sum = 0;
            for (int i = 0; i < data.length; i++) {
                if (type == Type.Column) {
                    sum += data[index][i];
                } else {
                    sum += data[i][index];
                }

            }
            return sum;
        });
    }
}

