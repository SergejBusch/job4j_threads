package ru.job4j.concurent.pool;

import org.junit.Test;
import ru.job4j.concurrent.pool.RolColSum;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RolColSumTest {

    private final int[][] matrix = {
            {1, 2, 3, 4},
            {1, 2, 3, 4},
            {1, 2, 3, 4},
            {1, 2, 3, 4}
    };

    @Test
    public void whenSequenceRun() {
        var array = RolColSum.sum(matrix);

        var result1 = Arrays.stream(
                array).map(RolColSum.Sums::getColSum).toArray();
        var result2 = Arrays.stream(
                array).map(RolColSum.Sums::getRowSum).toArray();

        assertThat(result1, is(new int[]{4, 8, 12, 16}));
        assertThat(result2, is(new int[]{10, 10, 10, 10}));
    }

    @Test
    public void whenAsyncRun() throws ExecutionException, InterruptedException {
        var array = RolColSum.asyncSum(matrix);

        var result1 = Arrays.stream(
                array).map(RolColSum.Sums::getColSum).toArray();
        var result2 = Arrays.stream(
                array).map(RolColSum.Sums::getRowSum).toArray();

        assertThat(result1, is(new int[]{4, 8, 12, 16}));
        assertThat(result2, is(new int[]{10, 10, 10, 10}));
    }
}
