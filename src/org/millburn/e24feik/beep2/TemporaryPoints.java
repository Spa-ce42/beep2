package org.millburn.e24feik.beep2;

import org.millburn.e24feik.beep2.util.ColoredPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TemporaryPoints {
    private volatile List<ColoredPoint> current;
    private List<ColoredPoint> future;
    private volatile boolean got;

    public TemporaryPoints() {
        this.current = Collections.unmodifiableList(new ArrayList<>());
        this.future = new ArrayList<>();
    }

    public void addPoint(int x, int y, int z, float r, float g, float b, float a) {
        this.future.add(new ColoredPoint(x, y, z, r, g, b, a));
    }

    public void flushPoints() {
        this.current = Collections.unmodifiableList(future);
        this.future = new ArrayList<>();
        this.got = false;
    }

    public List<ColoredPoint> getPoints() {
        this.got = true;
        return this.current;
    }

    public boolean got() {
        return this.got;
    }
}
