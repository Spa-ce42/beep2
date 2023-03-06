package org.millburn.e24feik.beep2.util;

public class Point {
    public final int x;
    public final int y;
    public final int z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceTo(Point c) {
        double d = c.x - this.x;
        double e = c.y - this.y;
        double f = c.z - this.z;
        return Math.sqrt(d * d + e * e + f * f);
    }

    @Override
    public int hashCode() {
        return this.z * 2500 + this.y * 50 + this.x;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(!(obj instanceof Point p)) {
            return false;
        }

        return this.x == p.x && this.y == p.y && this.z == p.z;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
