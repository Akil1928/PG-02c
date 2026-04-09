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
    //K
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
                new Item("Smart TV 65 pulg 4k", 1000, 20),
                new Item("PS5", 600, 2),
                new Item("Libro Java", 20, 1),
                new Item("Samsung Galaxy", 700, 1),
                new Item("Huawei", 400, 1),
                new Item("Libro C++", 25, 1),
                new Item("Xbox One", 500, 2),
                new Item("Drone", 500, 3),
                new Item("Proyector", 200, 3),
                new Item("LapTop", 800, 3),
                new Item("Impresora 3D", 800, 4),
                new Item("iPhone", 800, 1),
        };
    }

    public static Item[] Package2() {
        return new Item[]{
                new Item("Laptop",    4, 3),
                new Item("Libro",     3, 1),
                new Item("Cámara",    5, 2),
                new Item("Tablet",    4, 2),
                new Item("Audífonos", 2, 1),
                new Item("Cargador",  1, 1),
                new Item("Mochila",   6, 4),
        };
    }

    public static Item[] Package3() {
        return new Item[]{
                new Item("A", 3, 2),
                new Item("B", 4, 3),
                new Item("C", 5, 4),
                new Item("D", 6, 5),
        };
    }

    public static Item[] Package4() {
        return new Item[]{
                new Item("Item-1", 1, 1),
                new Item("Item-2", 6, 2),
                new Item("Item-3", 10, 3),
                new Item("Item-4", 16, 5),
        };
    }
}