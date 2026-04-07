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
    }

