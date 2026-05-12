package com.pao.laboratory03.exercise1;

import java.util.Locale;

/** Clasă abstractă de bază. DATĂ — nu modifica. */
public abstract class Shape {
    private String name;

    public Shape(String name) { this.name = name; }
    public String getName() { return name; }

    public abstract double area();
    public abstract double perimeter();

    @Override
    public String toString() {
        return name + " [area=" + String.format(Locale.US, "%.2f", area()) +
                ", perimeter=" + String.format(Locale.US, "%.2f", perimeter()) + "]";
    }
}
