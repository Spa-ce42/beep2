package org.millburn.e24feik.beep2;

import org.millburn.e24feik.beep2.core.Configuration;
import org.millburn.e24feik.beep2.core.Engine;
import org.millburn.e24feik.beep2.util.Point;

public final class Beep {
    private static final Configuration configuration;
    private static Engine engine;
    private static ComplexBeep complexBeep;
    private static Thread t;

    private Beep() {
        throw new AssertionError();
    }

    public static void setUpdatesPerSecond(int n) {
        if(n < 1) {
            throw new IllegalArgumentException("Updates per second (" + n + ") cannot be less than 0");
        }

        configuration.ups = n;
    }

    public static void init() {
        complexBeep = new ComplexBeep();
    }

    public static void setWidth(int width) {
        complexBeep.setWidth(width);
    }

    public static void setHeight(int height) {
        complexBeep.setHeight(height);
    }

    public static void setLength(int length) {
        complexBeep.setLength(length);
    }

    public static void setSize(int w, int h, int l) {
        complexBeep.setWidth(w);
        complexBeep.setHeight(h);
        complexBeep.setLength(l);
    }

    public static void setScale(double scale) {
        complexBeep.setScale((float)scale);
    }

    public static void addBee(int x, int y, int z) {
        complexBeep.addBee(new Point(x, y, z));
    }

    public static void removeBee(int x, int y, int z) {
        complexBeep.removeBee(new Point(x, y, z));
    }

    public static void addObstacle(int x, int y, int z) {
        complexBeep.addObstacle(new Point(x, y, z));
    }

    public static void removeObstacle(int x, int y, int z) {
        complexBeep.removeObstacle(new Point(x, y, z));
    }

    public static void addExit(int x, int y, int z) {
        complexBeep.addExit(new Point(x, y, z));
    }

    public static void removeExit(int x, int y, int z) {
        complexBeep.removeExit(new Point(x, y, z));
    }

    public static void addPath() {

    }

    public static void finishSetup() {
        complexBeep.finishSetup();
    }

    public static void show() {
        t = new Thread(() -> {
            engine = new Engine(configuration, complexBeep);
            engine.run();
        });

        t.start();
    }

    public static void addPoint(int x, int y, int z, float r, float g, float b, float a) {
        complexBeep.addPoint(x, y, z, r, g, b, a);
    }

    public static void end() {
        engine.clean();
    }

    static {
        configuration = new Configuration();
    }

    public static void flushPoints() {
        complexBeep.flushPoints();
    }
}
