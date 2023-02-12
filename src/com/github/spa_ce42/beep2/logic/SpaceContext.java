package com.github.spa_ce42.beep2.logic;

import java.util.Collections;
import java.util.List;

public class SpaceContext {
    public final int w, h, l;
    private List<Point> bees;
    private List<Point> obstacles;
    private List<Point> exits;

    public SpaceContext(int w, int h, int l) {
        this.w = w;
        this.h = h;
        this.l = l;
    }

    public List<Point> getBees() {
        return this.bees;
    }

    public void setBees(List<Point> bees) {
        this.bees = Collections.unmodifiableList(bees);
    }

    public List<Point> getObstacles() {
        return this.obstacles;
    }

    public void setObstacles(List<Point> obstacles) {
        this.obstacles = Collections.unmodifiableList(obstacles);
    }

    public List<Point> getExits() {
        return this.exits;
    }

    public void setExits(List<Point> exits) {
        this.exits = Collections.unmodifiableList(exits);
    }
}
