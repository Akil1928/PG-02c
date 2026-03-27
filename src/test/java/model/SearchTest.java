package model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    //TEST RECURSIVO – 20 elementos

    @Test
    void searchTest() {
        int[] arr = util.Utility.generatedSorted(20, 50);
        int value = arr[new Random().nextInt(arr.length)]; // valor garantizado en el arreglo

        Search.steps.clear();
        int pos = Search.binarySearch(arr, value, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
        System.out.println("Pos: " + pos + " Value: " + value);
        for (String s : Search.steps) {
            System.out.println(s);
        }

        assertTrue(pos >= 0, "Debe encontrar el valor");
        assertEquals(value, arr[pos]);
    }

    //TEST RECURSIVO – 35 elementos
    @Test
    void searchTest35() {
        int[] arr = util.Utility.generatedSorted(35, 100);
        int value = arr[new Random().nextInt(arr.length)];

        Search.steps.clear();
        int pos = Search.binarySearch(arr, value, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
        System.out.println("Pos: " + pos + " Value: " + value);
        for (String s : Search.steps) {
            System.out.println(s);
        }

        assertTrue(pos >= 0, "Debe encontrar el valor");
        assertEquals(value, arr[pos]);
    }

    //TEST RECURSIVO – 50 elementos
    @Test
    void searchTest50() {
        int[] arr = util.Utility.generatedSorted(50, 150);
        int value = arr[new Random().nextInt(arr.length)];

        Search.steps.clear();
        int pos = Search.binarySearch(arr, value, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
        System.out.println("Pos: " + pos + " Value: " + value);
        for (String s : Search.steps) {
            System.out.println(s);
        }

        assertTrue(pos >= 0, "Debe encontrar el valor");
        assertEquals(value, arr[pos]);
    }


    //TEST ITERATIVO – 20 elementos

    @Test
    void searchIterativeTest() {
        int[] arr = util.Utility.generatedSorted(20, 50);
        int value = arr[new Random().nextInt(arr.length)];

        Search.steps.clear();
        int pos = Search.binarySearchIterative(arr, value);

        System.out.println(Arrays.toString(arr));
        System.out.println("Pos: " + pos + " Value: " + value);
        for (String s : Search.steps) {
            System.out.println(s);
        }

        assertTrue(pos >= 0, "Debe encontrar el valor");
        assertEquals(value, arr[pos]);
    }


    //TEST ITERATIVO – 35 elementos
    @Test
    void searchIterativeTest35() {
        int[] arr = util.Utility.generatedSorted(35, 100);
        int value = arr[new Random().nextInt(arr.length)];

        Search.steps.clear();
        int pos = Search.binarySearchIterative(arr, value);

        System.out.println(Arrays.toString(arr));
        System.out.println("Pos: " + pos + " Value: " + value);
        for (String s : Search.steps) {
            System.out.println(s);
        }

        assertTrue(pos >= 0, "Debe encontrar el valor");
        assertEquals(value, arr[pos]);
    }


    //TEST ITERATIVO – 50 elementos

    @Test
    void searchIterativeTest50() {
        int[] arr = util.Utility.generatedSorted(50, 150);
        int value = arr[new Random().nextInt(arr.length)];

        Search.steps.clear();
        int pos = Search.binarySearchIterative(arr, value);

        System.out.println(Arrays.toString(arr));
        System.out.println("Pos: " + pos + " Value: " + value);
        for (String s : Search.steps) {
            System.out.println(s);
        }

        assertTrue(pos >= 0, "Debe encontrar el valor");
        assertEquals(value, arr[pos]);
    }
}