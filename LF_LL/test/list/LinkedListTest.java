package list;

import BSTthread.Pool;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LinkedListTest {
    private static LinkedList list;
    private static Pool pool;

    private static final int testSize = 1000000;
    private static List<Integer> numbers;

    @BeforeClass
    public static void init() throws Exception {
        numbers = IntStream.range(0, testSize).boxed().collect(Collectors.toList());
        Collections.shuffle(numbers);


    }

    @Before
    public void makeInstance() throws Exception {
        list = new LinkedList();
        pool = new Pool(4);
    }

    @Test
    public void insert() throws Exception {
        numbers.forEach((e) -> pool.push(() -> list.insert(e)));
        pool.join();
        numbers.forEach((e) -> assertTrue(list.search(e)));
    }

    @Test
    public void delete() throws Exception {
        numbers.forEach((e) -> list.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> list.delete((Integer) e)));
        pool.join();
        numbers.forEach((e) -> assertFalse(list.search(e)));
    }

    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> list.insert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> assertTrue(list.search(e))));
        pool.join();
    }
}