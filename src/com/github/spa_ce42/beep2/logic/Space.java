package com.github.spa_ce42.beep2.logic;

import com.github.spa_ce42.beep2.util.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Space {
    public static final byte AIR = 0;
    public static final byte BEE = 1;
    public static final byte OBS = 2;
    public static final byte EXT = 3;

    private final byte[][][] value;
    private final int w;
    private final int h;
    private final int l;

    private PriorityQueue<PointContext> openSet;
    private HashMap<Point, Point> cameFrom;
    private HashSet<Point> closedSet;
    private Point start;
    private Point end;

    public Space(SpaceContext sc) {
        this.w = sc.w;
        this.h = sc.h;
        this.l = sc.l;
        this.value = new byte[this.w][this.h][this.l];

        for(Point p : sc.getBees()) {
            this.value[p.x][p.y][p.z] = BEE;
        }

        for(Point p : sc.getObstacles()) {
            this.value[p.x][p.y][p.z] = OBS;
        }

        for(Point p : sc.getExits()) {
            this.value[p.x][p.y][p.z] = EXT;
        }

        System.out.println(Arrays.deepToString(this.value));
    }

    public int get(int x, int y, int z) {
        return this.value[x][y][z];
    }

    public List<Point> reconstructPath(HashMap<Point, Point> cameFrom, Point current) {
        List<Point> totalPath = new ArrayList<>();
        totalPath.add(current);

        while(cameFrom.containsKey(current)) {
            current = cameFrom.get(current);

            if(totalPath.contains(current)) {
                continue;
            }

            totalPath.add(current);
        }

        Collections.reverse(totalPath);

        return totalPath;
    }

    public boolean isValidEmptyCoordinates(int x, int y, int z) {
        if(x < 0 || x >= this.w) {
            return false;
        }

        if(y < 0 || y >= this.h) {
            return false;
        }

        if(z < 0 || z >= this.l) {
            return false;
        }

        return this.value[x][y][z] == AIR || this.value[x][y][z] == EXT;
    }

    private void addIfValid(List<Point> neighbors, int x, int y, int z) {
        if(isValidEmptyCoordinates(x, y, z)) {
            neighbors.add(new Point(x, y, z));
        }
    }

    public boolean isValidPath(List<Point> path, Point start) {
        for(Point p : path) {
            if(p.equals(start)) {
                continue;
            }

            if(!isValidEmptyCoordinates(p.x, p.y, p.z)) {
                switch(this.value[p.x][p.y][p.z]) {
                    case BEE -> System.out.println(p + " is a bee!");
                    case OBS -> System.out.println(p + " is an obstacle!");
                }
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    public List<Point> getNeighbors(Point Point) {
        List<Point> neighbors = new ArrayList<>();
        int x = Point.x;
        int y = Point.y;
        int z = Point.z;
//        this.addIfValid(neighbors, x - 1, y - 1, z - 1);
        this.addIfValid(neighbors, x - 1, y - 1, z + 0);
//        this.addIfValid(neighbors, x - 1, y - 1, z + 1);
        this.addIfValid(neighbors, x - 1, y + 0, z - 1);
        this.addIfValid(neighbors, x - 1, y + 0, z + 0);
        this.addIfValid(neighbors, x - 1, y + 0, z + 1);
//        this.addIfValid(neighbors, x - 1, y + 1, z - 1);
        this.addIfValid(neighbors, x - 1, y + 1, z + 0);
//        this.addIfValid(neighbors, x - 1, y + 1, z + 1);
        this.addIfValid(neighbors, x + 0, y - 1, z - 1);
        this.addIfValid(neighbors, x + 0, y - 1, z + 0);
        this.addIfValid(neighbors, x + 0, y - 1, z + 1);
        this.addIfValid(neighbors, x + 0, y + 0, z - 1);
        //this.addIfValid(neighbors, x + 0, y + 0, z + 0);
        this.addIfValid(neighbors, x + 0, y + 0, z + 1);
        this.addIfValid(neighbors, x + 0, y + 1, z - 1);
        this.addIfValid(neighbors, x + 0, y + 1, z + 0);
        this.addIfValid(neighbors, x + 0, y + 1, z + 1);
//        this.addIfValid(neighbors, x + 1, y - 1, z - 1);
        this.addIfValid(neighbors, x + 1, y - 1, z + 0);
//        this.addIfValid(neighbors, x + 1, y - 1, z + 1);
        this.addIfValid(neighbors, x + 1, y + 0, z - 1);
        this.addIfValid(neighbors, x + 1, y + 0, z + 0);
        this.addIfValid(neighbors, x + 1, y + 0, z + 1);
//        this.addIfValid(neighbors, x + 1, y + 1, z - 1);
        this.addIfValid(neighbors, x + 1, y + 1, z + 0);
//        this.addIfValid(neighbors, x + 1, y + 1, z + 1);
        return neighbors;
    }

    private void addIfInvalid(List<Point> points, int x, int y, int z) {
        if(!this.isValidEmptyCoordinates(x, y, z)) {
            points.add(new Point(x, y, z));
        }
    }

    public List<Point> getObstacleNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();
        int x = point.x;
        int y = point.y;
        int z = point.z;
        this.addIfInvalid(neighbors, x - 1, y - 1, z - 1);
        this.addIfInvalid(neighbors, x - 1, y - 1, z + 0);
        this.addIfInvalid(neighbors, x - 1, y - 1, z + 1);
        this.addIfInvalid(neighbors, x - 1, y + 0, z - 1);
        this.addIfInvalid(neighbors, x - 1, y + 0, z + 0);
        this.addIfInvalid(neighbors, x - 1, y + 0, z + 1);
        this.addIfInvalid(neighbors, x - 1, y + 1, z - 1);
        this.addIfInvalid(neighbors, x - 1, y + 1, z + 0);
        this.addIfInvalid(neighbors, x - 1, y + 1, z + 1);
        this.addIfInvalid(neighbors, x + 0, y - 1, z - 1);
        this.addIfInvalid(neighbors, x + 0, y - 1, z + 0);
        this.addIfInvalid(neighbors, x + 0, y - 1, z + 1);
        this.addIfInvalid(neighbors, x + 0, y + 0, z - 1);
        this.addIfInvalid(neighbors, x + 0, y + 0, z + 0);
        this.addIfInvalid(neighbors, x + 0, y + 0, z + 1);
        this.addIfInvalid(neighbors, x + 0, y + 1, z - 1);
        this.addIfInvalid(neighbors, x + 0, y + 1, z + 0);
        this.addIfInvalid(neighbors, x + 0, y + 1, z + 1);
        this.addIfInvalid(neighbors, x + 1, y - 1, z - 1);
        this.addIfInvalid(neighbors, x + 1, y - 1, z + 0);
        this.addIfInvalid(neighbors, x + 1, y - 1, z + 1);
        this.addIfInvalid(neighbors, x + 1, y + 0, z - 1);
        this.addIfInvalid(neighbors, x + 1, y + 0, z + 0);
        this.addIfInvalid(neighbors, x + 1, y + 0, z + 1);
        this.addIfInvalid(neighbors, x + 1, y + 1, z - 1);
        this.addIfInvalid(neighbors, x + 1, y + 1, z + 0);
        this.addIfInvalid(neighbors, x + 1, y + 1, z + 1);
        return neighbors;
    }

    public void aStarPrepare(Point start, Point end) {
        this.openSet = new PriorityQueue<>();
        openSet.add(new PointContext(start, start, end));
        this.cameFrom = new HashMap<>();
        this.closedSet = new HashSet<>();
        this.start = start;
        this.end = end;
    }

    public Triple<List<Point>, List<Point>, Boolean> aStarStep() {
        if(!openSet.isEmpty()) {
            PointContext current = openSet.poll();
            Point currentC = current.getCoordinates();
            List<Point> neighbors = this.getNeighbors(currentC);
            List<Point> rawNeighbors = this.getObstacleNeighbors(currentC);

            if(currentC.equals(end)) {
                List<Point> l = this.reconstructPath(cameFrom, currentC);
                l.add(end);
                return new Triple<>(l, rawNeighbors, true);
            }

            closedSet.add(currentC);
            for(Point neighborC : neighbors) {
                if(closedSet.contains(neighborC)) {
                    continue;
                }

                PointContext neighbor = new PointContext(start, neighborC, end);
                double tentativeGCost = current.getGCost() + currentC.distanceTo(neighborC);

                if(!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if(tentativeGCost >= neighbor.getGCost()) {
                    continue;
                }

                cameFrom.put(neighborC, currentC);
            }

            return new Triple<>(this.reconstructPath(cameFrom, currentC), rawNeighbors, false);
        } else {
            return new Triple<>(new ArrayList<>(), null, false);
        }
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public int getLength() {
        return this.l;
    }
}
