package demo;

import org.millburn.e24feik.beep2.util.Point;

public class PointContext implements Comparable<PointContext> {
    private final Point current;
    private final double fCost;
    private final double gCost;
    private final double hCost;

    public PointContext(Point start, Point current, Point end) {
        this.gCost = start.distanceTo(current);
        this.hCost = current.distanceTo(end);
        this.fCost = this.gCost + this.hCost;
        this.current = current;
    }

    public Point getCoordinates() {
        return this.current;
    }

    public double getGCost() {
        return this.gCost;
    }

    public double getFCost() {
        return this.fCost;
    }

    public double getHCost() {
        return this.hCost;
    }

    @Override
    public int compareTo(PointContext o) {
        double delta = this.fCost - o.fCost;
        double absDelta = Math.abs(delta);

        if(0 < absDelta && absDelta < 1) {
            return delta > 0 ? 1 : -1;
        }

        return (int)delta;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }

        if(!(obj instanceof PointContext c)) {
            return false;
        }

        return this.current.equals(c.current);
    }

    @Override
    public String toString() {
        return this.current.toString();
    }

}
