package reportTest;


import bst.RWBST;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class RWbstTest {
    private RWBST tree;
    private thread.Pool pool;

    private int threadSize;
    private int[] searchRatio;
    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    @BeforeClass
    public static void init() throws Exception {
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);
    }

    @Before
    public void makeInstance() throws Exception {
        tree = new RWBST();
        pool = new thread.Pool(threadSize);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1}, {2}, {4}, {8}
        });
    }

    public RWbstTest(int threadSize) {
        this.threadSize = threadSize;
        this.searchRatio = new int[]{ 1, 4, 9 };
    }

    private static long executeWithTime(Runnable task) {
        long start = System.currentTimeMillis();
        task.run();
        return System.currentTimeMillis() - start;
    }

    private static boolean assertRatio(int t, int f, int v) {
        return (v % (t + f)) < t;
    }

    @Test
    public void testA() throws Exception { // Insert
        long time = executeWithTime(() -> {
            numbers.forEach((e) -> pool.push(() -> tree.parRWLockInsert(e)));
            pool.join();
        });
    }

    @Test
    public void testB() throws Exception { // Insert ~ Search Ratio
        long insertTime = executeWithTime(() -> {
            numbers.forEach((e) -> pool.push(() -> tree.parRWLockInsert(e)));
            pool.join();
        });

        long[] result = new long[this.searchRatio.length];
        for (int i = 0; i < searchRatio.length; ++i) {
            final int ratio = searchRatio[i];
            result[i] = executeWithTime(() -> {
                numbers.forEach((e) -> {
                    if (assertRatio(1, ratio, e)) pool.push(() -> tree.parRWLockInsert(e));
                    else pool.push(() -> tree.parRWLockSearch(e));
                });
                pool.join();
            });
            System.out.println(result[i]);
        }
    }
}