package org.millburn.e24feik.beep2.util;

public class ColoredPoint extends Point {
    public final float r, g, b, a;

    public ColoredPoint(int x, int y, int z, float r, float g, float b, float a) {
        super(x, y, z);

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
