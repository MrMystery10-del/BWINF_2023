package de.arukone;

import java.util.Objects;

/**
 * A class that represents a node in a grid.
 * A node has coordinates, distance from the origin, cost, obstacle status, child node, visited status, and horizontal flag.
 * A node can be compared to another node based on the sum of its distance and cost.
 */
public class Node implements Comparable<Node> {

    // The x-coordinate of the node
    private final int x;
    // The y-coordinate of the node
    private final int y;
    // The distance of the node from the origin
    private final int originDistance;
    // The cost of the node
    private final int cost;
    // The obstacle status of the node
    private final boolean obstacle;

    // The child node of the current node
    private Node child;
    // The visited status of the node
    private boolean visited;
    // The horizontal flag of the node
    private boolean horizontal;

    /**
     * Constructs a new node with the given parameters.
     * @param x The x-coordinate of the node
     * @param y The y-coordinate of the node
     * @param originDistance The distance of the node from the origin
     * @param cost The cost of the node
     * @param obstacle The obstacle status of the node
     */
    public Node(int x, int y, int originDistance, int cost, boolean obstacle) {
        this.x = x;
        this.y = y;
        this.originDistance = originDistance;
        this.cost = cost;
        this.obstacle = obstacle;
    }

    /**
     * Returns the x-coordinate of the node.
     * @return The x-coordinate of the node
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the node.
     * @return The y-coordinate of the node
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the distance of the node from the origin.
     * @return The distance of the node from the origin
     */
    public int getOriginDistance() {
        return originDistance;
    }

    /**
     * Returns the cost of the node.
     * @return The cost of the node
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns the obstacle status of the node.
     * @return True if the node is an obstacle, false otherwise
     */
    public boolean isObstacle() {
        return obstacle;
    }

    /**
     * Returns the child node of the current node.
     * @return The child node of the current node
     */
    public Node getChild() {
        return child;
    }

    /**
     * Sets the child node of the current node.
     * @param child The child node to be set
     */
    public void setChild(Node child) {
        this.child = child;
    }

    /**
     * Returns the visited status of the node.
     * @return True if the node has been visited, false otherwise
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Sets the visited status of the node.
     * @param visited The visited status to be set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Returns the horizontal flag of the node.
     * @return True if the node is horizontal, false otherwise
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Sets the horizontal flag of the node.
     * @param horizontal The horizontal flag to be set
     */
    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * Compares this node to another node based on their sum of distance and cost.
     * @param anotherNode The other node to be compared with
     * @return A negative integer, zero, or a positive integer as this sum is less than, equal to, or greater than anotherNode's sum
     */
    @Override
    public int compareTo(Node anotherNode) {
        return Integer.compare(originDistance + cost, anotherNode.originDistance + anotherNode.cost);
    }

    /**
     * Checks if this object is equal to another object based on their coordinates.
     * @param o The other object to be checked for equality with
     * @return True if this object is equal to o, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node that = (Node) o;
        return x == that.x && y == that.y;
    }

    /**
     * Returns the hash code of this object based on its coordinates.
     * @return The hash code of this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}