package model;

public class Item {
    private final String name;
    private final int weight;
    private final int value;

    public Item(String name, int value, int weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }
    public String getName() { return name; }
    public int getValue() { return value; }
    public int getWeight() { return weight; }
    public double getRatio() { return (double)value / weight; }

    @Override
    public String toString() {
        return String.format("%s (v=%d, w=%d, r=%.2f)", name, value, weight, getRatio());
    }

    public static Item[] Package1() {
        return new Item[]{
                new Item("Smart TV 65 pulg 4k", 20, 1000),
                new Item("PS5", 2, 600),
                new Item("Libro Java", 1, 20),
                new Item("Samsung Galaxy", 1, 700),
                new Item("Huawei", 1, 400),
                new Item("Libro C++", 1, 25),
                new Item("Xbox One", 2, 500),
                new Item("Drone", 3, 500),
                new Item("Proyector", 3, 200),
                new Item("LapTop", 3, 800),
                new Item("Impresora 3D", 4, 800),
                new Item("iPhone", 1, 800),
        };
    }

    public static Item[] Package2() {
        return new Item[]{
                new Item("Laptop",    3, 4),
                new Item("Libro",     1, 3),
                new Item("Cámara",    2, 5),
                new Item("Tablet",    2, 4),
                new Item("Audífonos", 1, 2),
                new Item("Cargador",  1, 1),
                new Item("Mochila",   4, 6),
        };
    }

    public static Item[] Package3() {
        return new Item[]{
                new Item("A", 2, 3),
                new Item("B", 3, 4),
                new Item("C", 4, 5),
                new Item("D", 5, 6),
        };
    }

    public static Item[] Package4() {
        return new Item[]{
                new Item("Item-1", 1, 1),
                new Item("Item-2", 2, 6),
                new Item("Item-3", 3, 10),
                new Item("Item-4", 5, 16),
        };
    }
    }

