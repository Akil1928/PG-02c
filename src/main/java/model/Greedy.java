package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Greedy {

    private static final int[] monedas = {500, 100, 50, 25, 10, 5, 1};

    public static List<Integer> coinChange(int monto) {
        List<Integer> usadas = new ArrayList<>();
        for (int moneda : monedas) {
            while (monto >= moneda) {
                usadas.add(moneda);
                monto -= moneda;
            }
        }
        return usadas;
    }

    public static String coinChangeString(int montoOriginal) {
        StringBuilder result = new StringBuilder();
        int restante = montoOriginal;

        for (int moneda : monedas) {
            int cantidad = restante / moneda;
            if (cantidad > 0) {
                int valorUsado = cantidad * moneda;
                int nuevoRestante = restante - valorUsado;

                result.append(moneda)
                        .append(" x ")
                        .append(cantidad)
                        .append(" = ")
                        .append(valorUsado)
                        .append(" (resta ")
                        .append(nuevoRestante)
                        .append(")\n");
                restante = nuevoRestante;
            }
        }
        return result.toString();
    }

    public static class KnapsackResult {
        public final Item[] sortedItems; //lista inicial de articulos
        public final List<Item> selected; //elementos agregados a la mochila
        public final double maxValue; //ganancia maxima
        public final double maxWeight; //peso maximo agregado a la mochila
        public final int capacity;
        public final long nanoTime;

        public KnapsackResult(Item[] sortedItems, List<Item> selected, double maxValue, double maxWeight, int capacity, long nanoTime) {
            this.sortedItems = sortedItems;
            this.selected = selected;
            this.maxValue = maxValue;
            this.maxWeight = maxWeight;
            this.capacity = capacity;
            this.nanoTime = nanoTime;
        }
    }

    public static KnapsackResult knapsackSolve(Item[] items, int mcapacity) {
        long t1 = System.nanoTime();
        //copiar y ordenar el arreglo por ratio v/w descendente

        Item[] sortedItems = items.clone();
        Arrays.sort(sortedItems, (a, b) -> Double.compare(b.getRatio(), a.getRatio()));
return null;
    }

    public static void BubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}