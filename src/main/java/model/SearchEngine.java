package model;

import java.util.Arrays;
import java.util.Random;

/**
 * Motor de búsqueda: implementa el algoritmo de búsqueda binaria capturando
 * cada comparación como un Step para la animación.
 */
public class SearchEngine {

    //BÚSQUEDA BINARIA  –  O(log n)  [requiere arreglo ordenado]
    public SearchResult binary(int[] array, int target) {
        long t0 = System.nanoTime();
        int lo = 0, hi = array.length - 1;
        int found = -1, comps = 0;

        SearchResult result = new SearchResult(array, target, -1, 0);

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comps++;

            if (array[mid] == target) {
                result.steps.add(new SearchResult.Step(
                    mid, lo, hi, mid,
                    "¡Encontrado! arr[" + mid + "] = " + target +
                    "  (rango [" + lo + "," + hi + "])", true));
                found = mid;
                break;
            } else if (array[mid] < target) {
                result.steps.add(new SearchResult.Step(
                    mid, lo, hi, mid,
                    "arr[" + mid + "]=" + array[mid] + " < " + target +
                    " → buscar en [" + (mid+1) + "," + hi + "]", false));
                lo = mid + 1;
            } else {
                result.steps.add(new SearchResult.Step(
                    mid, lo, hi, mid,
                    "arr[" + mid + "]=" + array[mid] + " > " + target +
                    " → buscar en [" + lo + "," + (mid-1) + "]", false));
                hi = mid - 1;
            }
        }

        if (found < 0) {
            result.steps.add(new SearchResult.Step(
                -1, lo, hi, -1,
                "No encontrado. Rango agotado.", false));
        }

        long nanos = System.nanoTime() - t0;
        return new SearchResult(array, target, found, nanos, comps) {{
            steps.addAll(result.steps);
        }};
    }


    //GENERADOR DE ARREGLOS
    /** Arreglo ordenado con valores uniformemente distribuidos. */
    public static int[] generateSorted(int size, int maxVal) {
        int[] arr = new Random().ints(size, 1, maxVal + 1)
                               .distinct().limit(size).sorted().toArray();
        if (arr.length < size) {
            //fallback si no hay suficientes distintos
            arr = new int[size];
            for (int i = 0; i < size; i++) arr[i] = (i + 1) * (maxVal / size);
        }
        return arr;
    }

    /** Inserta el target en una posición aleatoria del arreglo ordenado. */
    public static int[] ensureContains(int[] sorted, int target) {
        for (int v : sorted) if (v == target) return sorted;
        int[] copy = Arrays.copyOf(sorted, sorted.length);
        copy[sorted.length / 2] = target;
        Arrays.sort(copy);
        return copy;
    }
}
