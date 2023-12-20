package de.arukone;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Grid {

    private static final Random random = new Random();
    private final int[] grid;
    private int pair = 1;

    public final int width;

    /**
     * generates a new solvable Arukone grid
     */
    public Grid(int size) {
        this.grid = new int[size * size];
        this.width = size;

        int trapYPos = setTrap();
        generateRandomPairs(trapYPos);

        randomRotate();
    }

    /**
     * @return String representation of the grid
     */
    public String getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                builder.append(getValue(x, y)).append(" ");
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    // Sets a two-pair trap for the Arukone solver with small variation
    private int setTrap() {
        boolean fromLeft = (random.nextInt() & 1) == 0;
        int x, y = random.nextInt(1, width - 2);

        if (fromLeft) {
            x = 0;

            setValue(x, y + random.nextInt(0, 3), pair);
            setValue(width - 1, y + 1, pair);
            pair++;

            setValue(1, y, pair);
            setValue(width - 1, y + 2, pair);
        } else {
            x = width - 1;

            setValue(x, y + random.nextInt(0, 3), pair);
            setValue(0, y + 1, pair);
            pair++;

            setValue(x - 1, y, pair);
            setValue(0, y + 2, pair);
        }
        pair++;
        return y - 1;
    }

    // A method that randomly rotates the grid
    private void randomRotate() {
        int n = (int) (Math.random() * 3);

        // Rotate the grid n times by 90 degrees clockwise
        for (int i = 0; i < n; i++) rotate90();
    }

    // A helper method that rotates the grid by 90 degrees clockwise
    private void rotate90() {
        int[][] newGrid = new int[width][width];

        // Loop through the original grid and copy the values to the new grid
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                // The value at (i, j) in the original grid will be at (j, width - i - 1) in the new grid
                newGrid[j][width - i - 1] = getValue(i, j);
            }
        }

        // Loop through the new grid and set the values to the original grid
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                setValue(i, j, newGrid[i][j]);
            }
        }
    }

    // A method to generate random pairs of points and connect them with a path
    private void generateRandomPairs(int trapYPos) {
        List<Node> takenNodes = new ArrayList<>();

        // Loop until the pair variable reaches the width
        while (pair < width - 1) {
            Point[] points = generateRandomPoints(trapYPos, takenNodes);

            // Get the path between the points using AStar algorithm
            Stack<Node> nodes = AStar.getPath(this, points[0], points[1]);

            // Check if the path is valid
            if (nodes == null || nodes.isEmpty()) {
                resetValues(points);
            } else if (isPathInvalid(nodes, takenNodes, trapYPos)) {
                resetValues(points);
            } else {
                setValues(points, pair);

                takenNodes.addAll(nodes);
                pair++;
            }
        }
    }

    // A method to generate two random points that are not in the trap area and not already taken
    private Point[] generateRandomPoints(int trapYPos, List<Node> takenPoints) {
        Point[] points = new Point[2];

        for (int i = 0; i < 2; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(width);

            // Loop until a valid point is found
            while (true) {
                // Check if the point is in the trap area
                if (y > trapYPos - 1 && y < (trapYPos + 4)) {
                    y = random.nextInt(width);
                } else if (points[0] != null) {
                    // Check if the point is on the opposite side of the trap area from the first point
                    if (points[0].y < trapYPos && y > trapYPos || points[0].y > trapYPos && y < trapYPos) {
                        y = random.nextInt(width);
                    } else break;
                } else break;
            }
            // Check if the point is already taken, if so, retry the process
            int finalY = y;
            if (getValue(x, y) != 0 || takenPoints.stream().anyMatch(node -> node.getX() == x && node.getY() == finalY)) {
                i--;
                continue;
            }
            ;

            // Assign the point to the array
            points[i] = new Point(x, y);
        }
        return points;
    }

    // A method to reset the values of two points to zero
    private void resetValues(Point[] points) {
        for (Point point : points) {
            setValue(point.x, point.y, 0);
        }
    }

    // A method to set the values of two points to a given value
    private void setValues(Point[] points, int value) {
        for (Point point : points) {
            setValue(point.x, point.y, value);
        }
    }

    // A method to check if a path is invalid, i.e. it crosses the trap area or overlaps with another path
    private boolean isPathInvalid(Stack<Node> nodes, List<Node> takenNodes, int trapYPos) {
        return nodes.stream().anyMatch(node -> node.getY() > trapYPos && node.getY() < (trapYPos + 4)) ||
                !Collections.disjoint(takenNodes, nodes);
    }

    public void setValue(int x, int y, int value) {
        if (inBounds(x, y))
            grid[width * y + x] = value;
    }

    public int getValue(int x, int y) {
        if (inBounds(x, y))
            return grid[width * y + x];
        return -1;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < width;
    }
}