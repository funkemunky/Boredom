package cc.funkemunky.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Test {

    private static List<Integer> list = new ArrayList<>();
    public static void main(String[] args) {

        long timeStamp = System.currentTimeMillis();
        computeParallel(100);
        long elapsed = System.currentTimeMillis() - timeStamp;


        System.out.println("END: " + elapsed + "ms");
    }

    static IntStream getComputation() {
        return IntStream
                .generate(() -> ThreadLocalRandom.current().nextInt(2147483647));
    }

    static void computeSequential(int target) {
        IntStream.range(0, target)
                .forEach(loop -> {
                    final int result = getComputation()
                            .parallel()
                            .filter(i -> i <= target)
                            .findAny()
                            .getAsInt();
                    System.out.println(result);
                });
    }

    static void computeParallel(int target) {
        IntStream.range(0, target)
                .parallel()
                .forEach(loop -> {
                    final int result = getComputation()
                            .parallel()
                            .filter(i -> i <= target)
                            .findAny()
                            .getAsInt();
                    System.out.println(result);
                });
    }
}
