package de.zauberschule;

/**
 * A class that represents a node for the A-Star algorithm.
 * A node can be compared to another node based on the sum of its distance and cost.
 */
public class Node implements Comparable<Node> {

    private final Integer x, y;
    private Integer originDistance, totalCost;

    private final boolean obstacle, secondFloor;

    private Node child;

    private boolean visited, haveAdditionalCost;

    /**
     * Constructs a new node with the given parameters.
     *
     * @param x        The x-coordinate of the node
     * @param y        The y-coordinate of the node
     * @param obstacle The obstacle status of the node
     */
    public Node(int x, int y, boolean obstacle, boolean secondFloor) {
        this.x = x;
        this.y = y;
        this.obstacle = obstacle;
        this.secondFloor = secondFloor;
    }

    /**
     * Sets the distance of the node to the origin.
     *
     * @param distance The origin distance to be set
     */
    protected void setOriginDistance(int distance) {
        this.originDistance = distance;
    }

    /**
     * Sets the total cost of the node.
     *
     * @param cost The total cost to be set
     */
    protected void setTotalCost(int cost) {
        this.totalCost = cost;
    }

    /**
     * Sets the child node of the current node.
     *
     * @param child The child node to be set
     */
    protected void setChild(Node child) {
        this.child = child;
    }

    /**
     * Sets the visited status of the node.
     *
     * @param visited The visited status to be set
     */
    protected void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Returns the distance of the node from the origin.
     *
     * @return The distance of the node from the origin
     */
    public int getOriginDistance() {
        return originDistance;
    }

    /**
     * Returns the total cost of the node.
     *
     * @return The cost of the node
     */
    public int getTotalCost() {
        return totalCost;
    }

    /**
     * Returns the child node of the current node.
     *
     * @return The child node of the current node
     */
    protected Node getChild() {
        return child;
    }

    /**
     * Returns the obstacle status of the node.
     *
     * @return True if the node is an obstacle, false otherwise
     */
    public boolean isObstacle() {
        return obstacle;
    }

    /**
     * Returns the floor status of the node.
     *
     * @return True if the nodeis located in the second floor, false when on first floor
     */
    public boolean isSecondFloor() {
        return secondFloor;
    }

    /**
     * Returns the visited status of the node.
     *
     * @return True if the node has been visited, false otherwise
     */
    protected boolean isVisited() {
        return visited;
    }

    /**
     * Compares this node to another node based on their sum of distance and cost.
     *
     * @param anotherNode The other node to be compared with
     * @return A negative integer, zero, or a positive integer as this sum is less than, equal to, or greater than anotherNode's sum
     */
    @Override
    public int compareTo(Node anotherNode) {
        return Integer.compare(originDistance + totalCost, anotherNode.originDistance + anotherNode.totalCost);
    }

    /**
     * Checks if this object is equal to another object based on their coordinates.
     *
     * @param o The other object to be checked for equality with
     * @return True if this object is equal to o, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;

        if (secondFloor != node.secondFloor) return false;
        if (!x.equals(node.x)) return false;
        return y.equals(node.y);
    }

    /**
     * Returns the hash code of this object based on its coordinates.
     *
     * @return The hash code of this object
     */
    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + (secondFloor ? 1 : 0);
        return result;
    }
}
