package reportTest;


import bst.RWBST;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class RWbstInsertSearchTest {
    private static RWBST tree;
    private static thread.Pool pool;

    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    @BeforeClass
    public static void init() throws Exception {
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);

        pool = new thread.Pool(4);
    }

    @Before
    public void makeInstance() throws Exception {
        tree = new RWBST();
    }

    @Test
    public void insert() throws Exception {
        numbers.forEach((e) -> tree.parRWLockInsert(e));              // insert 후
        numbers.forEach((e) -> assertTrue(tree.parRWLockSearch(e)));  // search로 제대로 되었는지 확인
    }

    @Test
    public void insertParallel() throws Exception {
        numbers.forEach((e) -> pool.push(() -> tree.parRWLockInsert(e)));
        pool.join();
        numbers.forEach((e) -> assertTrue(tree.parRWLockSearch(e)));
    }

    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> tree.parRWLockInsert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.parRWLockSearch(e));
    }

    @Test
    public void searchParallel() throws Exception {
        numbers.forEach((e) -> tree.parRWLockInsert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> assertTrue(tree.parRWLockSearch(e))));
        pool.join();
    }
}