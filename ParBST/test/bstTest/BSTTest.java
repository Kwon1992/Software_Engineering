package bstTest;

import bst.BST;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BSTTest {
    private static BST tree;
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
        tree = new BST();
    }

    @Test
    public void insert() throws Exception {
        numbers.forEach((e) -> tree.parInsert(e));              // insert 후
        numbers.forEach((e) -> assertTrue(tree.parSearch(e)));  // search로 제대로 되었는지 확인
    }

    @Test
    public void insertParallel() throws Exception {
        numbers.forEach((e) -> pool.push(() -> tree.parInsert(e)));
        pool.join();
        numbers.forEach((e) -> assertTrue(tree.parSearch(e)));
    }

    @Test
    public void delete() throws Exception {
        numbers.forEach((e) -> tree.parInsert(e));  //삽입 후
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.parDelete(e));  //삭제
        numbers.forEach((e) -> assertFalse(tree.parSearch(e)));    // 전부 비어있는지 확인.
    }

    @Test
    public void deleteParallel() throws Exception {
        numbers.forEach((e) -> tree.parInsert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> tree.parDelete(e)));
        pool.join();
        numbers.forEach((e) -> assertFalse(tree.parSearch(e)));
    }

    @Test
    public void search() throws Exception {
        numbers.forEach((e) -> tree.parInsert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> tree.parSearch(e));
    }

    @Test
    public void searchParallel() throws Exception {
        numbers.forEach((e) -> tree.parInsert(e));
        Collections.shuffle(numbers);
        numbers.forEach((e) -> pool.push(() -> assertTrue(tree.parSearch(e))));
        pool.join();
    }
}