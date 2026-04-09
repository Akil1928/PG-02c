package model;

import java.util.List;

public class KnapsackTest {

    public static void main(String[] args) {
        // Capacidades de mochila a probar
        int[] capacities = {15, 12, 10, 7};

        // Paquetes de items a probar
        Item[][] packages = {
                Item.Package1(),
                Item.Package2(),
                Item.Package3(),
                Item.Package4()
        };

        String[] packageNames = {"Package1", "Package2", "Package3", "Package4"};

        System.out.println("    PROBLEMA DE LA MOCHILA - ALGORITMO VORAZ (GREEDY)");

        // Probar cada paquete con cada capacidad
        for (int i = 0; i < packages.length; i++) {
            System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
            System.out.println("│  " + packageNames[i] + " - Items Disponibles");
            System.out.println("└─────────────────────────────────────────────────────────────┘");

            printItems(packages[i]);

            for (int capacity : capacities) {
                testKnapsack(packages[i], capacity, packageNames[i]);
            }

            System.out.println("\n" + "─".repeat(65));
        }

        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println("                    FIN DEL TESTEO");
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    private static void printItems(Item[] items) {
        System.out.println("\n  Items disponibles:");
        for (Item item : items) {
            System.out.println("    • " + item);
        }
        System.out.println();
    }

    private static void testKnapsack(Item[] items, int capacity, String packageName) {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║  Capacidad de Mochila: " + capacity + " kg");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");

        // Resolver el problema
        Greedy.KnapsackResult result = Greedy.knapsackSolve(items, capacity);

        // Mostrar items ordenados por ratio
        System.out.println("\n  ➤ Items ordenados por ratio (valor/peso):");
        for (int i = 0; i < result.sortedItems.length; i++) {
            System.out.println("    " + (i + 1) + ". " + result.sortedItems[i]);
        }

        // Mostrar items seleccionados
        System.out.println("\n  ➤ Items seleccionados para la mochila:");
        if (result.selected.isEmpty()) {
            System.out.println("    ⚠ Ningún item cabe en la mochila");
        } else {
            for (int i = 0; i < result.selected.size(); i++) {
                Item item = result.selected.get(i);
                System.out.println("    ✓ " + (i + 1) + ". " + item);
            }
        }

        // Mostrar resultados
        System.out.println("\n  ➤ Resultados:");
        System.out.println("    • Valor total máximo: $" + (int)result.maxValue);
        System.out.println("    • Peso total usado: " + (int)result.maxWeight + " kg");
        System.out.println("    • Capacidad disponible: " + capacity + " kg");
        System.out.println("    • Espacio restante: " + (capacity - (int)result.maxWeight) + " kg");
        System.out.println("    • Tiempo de ejecución: " + formatTime(result.nanoTime));
        System.out.println("    • Eficiencia: " + String.format("%.2f%%",
                ((double)result.maxWeight / capacity) * 100));
    }

    private static String formatTime(long nanoTime) {
        if (nanoTime < 1000) {
            return nanoTime + " ns";
        } else if (nanoTime < 1_000_000) {
            return String.format("%.2f µs", nanoTime / 1000.0);
        } else {
            return String.format("%.2f ms", nanoTime / 1_000_000.0);
        }
    }
}