package model;

import java.util.ArrayList;
import java.util.List;

public class Search {

    public static List<String> steps = new ArrayList<>();

    //BÚSQUEDA BINARIA RECURSIVA  –  O(log n)
    public static int binarySearch(int[] sortedArray, int value, int low, int high) {
        // Caso base: rango agotado
        if (low > high) {
            steps.add("No encontrado");
            return -1;
        }

        int mid = low + (high - low) / 2;

        steps.add("Rango [" + low + "," + high + "] -->mid=" + mid
                + " (sortedArray[mid]=" + sortedArray[mid] + ")");

        if (value == sortedArray[mid]) {
            steps.add("Encontrado en índice " + mid);
            return mid;

        } else if (value < sortedArray[mid]) {
            // Backtracking: mitad izquierda
            return binarySearch(sortedArray, value, low, mid - 1);

        } else {
            // Backtracking: mitad derecha
            return binarySearch(sortedArray, value, mid + 1, high);
        }
    }


    //BÚSQUEDA BINARIA ITERATIVA  –  O(log n)
    public static int binarySearchIterative(int[] sortedArray, int value) {
        int low = 0;
        int high = sortedArray.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            steps.add("Rango [" + low + "," + high + "] -->mid=" + mid
                    + " (sortedArray[mid]=" + sortedArray[mid] + ")");

            if (value == sortedArray[mid]) {
                steps.add("Encontrado en índice " + mid);
                return mid;

            } else if (value < sortedArray[mid]) {
                high = mid - 1;

            } else {
                low = mid + 1;
            }
        }

        steps.add("No encontrado");
        return -1;
    }
}