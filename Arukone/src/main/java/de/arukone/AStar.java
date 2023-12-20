package de.arukone;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class AStar {

    // A method that returns a stack of nodes that represents the path from the start point to the end point in a grid
    public static Stack<Node> getPath(Grid grid, Point start, Point end) {
        // A list of nodes that are currently being considered for the path
        List<Node> currentNodes = new ArrayList<>(grid.width);
        // A two-dimensional array of nodes that are calculated from the grid, the start point, and the end point
        Node[][] nodes = calculateNodes(grid, start, end);

        // Add the node at the start point to the current nodes list
        currentNodes.add(nodes[start.x][start.y]);
        currentNodes.get(0).setVisited(true);

        Node endNode = nodes[end.x][end.y];

        // Loop until the first node in the current nodes list is the end node
        while (currentNodes.get(0).getX() != end.x || currentNodes.get(0).getY() != end.y) {
            // Get the cheapest neighbors
            List<Node> neighbors = getCheapestNeighbors(currentNodes, nodes, endNode);

            // If there are no neighbors, then there is no path
            if (neighbors == null) {
                // Get the longest distance of the first node in the current nodes list
                int longestDistance = currentNodes.get(0).getOriginDistance();

                // Loop through all possible distances
                for (int i = 0; i < longestDistance; i++) {
                    int finalI = i;
                    // Find the first node in the nodes array that has the same distance as i
                    Optional<Node> cheapestOption = Arrays.stream(nodes).flatMap(Arrays::stream).filter(node -> node.getOriginDistance() == finalI).findFirst();
                    // If such a node exists, clear the current nodes list and add that node to it
                    if (cheapestOption.isPresent()) {
                        currentNodes.clear();
                        currentNodes.add(cheapestOption.get());

                        neighbors = getCheapestNeighbors(currentNodes, nodes, endNode);

                        if (neighbors != null) break;
                    }
                }
                // If there are still no neighbors, return null (no path)
                if (neighbors == null) return null;
            }
            // If the first neighbor is the end node, clear the current nodes list and add that neighbor to it
            else if (neighbors.get(0).equals(endNode)) {
                currentNodes.clear();
                currentNodes.add(neighbors.get(0));
                break;
            }
            // Otherwise, clear the current nodes list and add all the neighbors to it
            else {
                currentNodes.clear();
                currentNodes.addAll(neighbors);
            }
        }

        Stack<Node> path = new Stack<>();
        // Loop until the first node in the current nodes list is the start node
        while (currentNodes.get(0).getX() != start.x || currentNodes.get(0).getY() != start.y) {
            // Push the first node in the current nodes list to the path stack
            path.push(currentNodes.get(0));
            // Get the child node of the first node in the current nodes list
            Node child = currentNodes.get(0).getChild();

            // If the child node is not null, set it as the first node in the current nodes list
            if (child != null) {
                currentNodes.set(0, child);
            }
            // Otherwise, break out of the loop
            else break;
        }
        // Return the path stack
        return path;
    }


    // A method that returns a list of the cheapest neighbors of a list of current nodes in a grid
    private static List<Node> getCheapestNeighbors(List<Node> currentNodes, Node[][] nodes, Node endNode) {
        // Use a list of offsets to avoid nested loops
        int[][] offsets = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        // Create a list of neighbors to store the valid and unvisited nodes around the current nodes
        List<Node> neighbors = new ArrayList<>(nodes.length);

        for (Node currentNode : currentNodes) {
            for (int[] offset : offsets) {
                // Add the offset to the current node coordinates
                int x = currentNode.getX() + offset[0];
                int y = currentNode.getY() + offset[1];
                // Check if the node is valid and add it to the list
                if (inBounds(x, y, nodes.length)) {
                    // Get the node from the matrix
                    Node node = nodes[x][y];

                    // If the node is not an obstacle and not visited, add it to the neighbors list and set its properties
                    if (!node.isObstacle() && !node.isVisited()) {
                        node.setHorizontal(offset[0] != 0);
                        node.setVisited(true);
                        node.setChild(currentNode);
                        neighbors.add(node);
                    }
                    // If the node is equal to the end node, set its child and return a singleton list containing it
                    else if (node.equals(endNode)) {
                        endNode.setChild(currentNode);
                        return List.of(endNode);
                    }
                }
            }
        }

        // If the neighbor list is not empty, return the lowest cost neighbors from it
        if (!neighbors.isEmpty()) {
            return getLowestCostNeighbors(neighbors);
        }
        // Otherwise, return null
        else return null;
    }



    private static List<Node> getLowestCostNeighbors(List<Node> neighbors) {
        Comparator<Node> nodeComparator = Comparator.comparingInt(Node::getCost)
                .thenComparingInt(Node::getOriginDistance);
        Optional<Node> minNode = neighbors.stream().min(nodeComparator);
        if (minNode.isPresent()) {
            // Get the minimum cost and origin distance from the minNode
            int lowestCost = minNode.get().getCost();
            int lowestOriginDistance = minNode.get().getOriginDistance();
            // Return a list of nodes that match the minimum values
            return neighbors.stream()
                    .filter(node -> node.getCost() == lowestCost && node.getOriginDistance() == lowestOriginDistance)
                    .collect(Collectors.toList());
        } else {
            // Return an empty list if the input list is empty
            return Collections.emptyList();
        }
    }


    // A method that returns a two-dimensional array of nodes that are calculated from a grid, a start point, and an end point
    private static Node[][] calculateNodes(Grid grid, Point start, Point end) {
        // Create a new array of nodes with the same size as the grid
        Node[][] nodes = new Node[grid.width][grid.width];

        for (int x = 0; x < grid.width; x++) {
            for (int y = 0; y < grid.width; y++) {
                // Calculate the distance of the node from the start point
                int originDistance = Math.abs(start.x - x) + Math.abs(start.y - y);
                // Calculate the distance of the node from the end point
                int goalDistance = Math.abs(end.x - x) + Math.abs(end.y - y);
                // Check if the node is an obstacle based on the grid value
                boolean isObstacle = grid.getValue(x, y) != 0;

                // Create a new node with the calculated parameters and assign it to the array
                nodes[x][y] = new Node(x, y, originDistance, originDistance + goalDistance, isObstacle);
            }
        }

        return nodes;
    }


    private static boolean inBounds(int x, int y, int width) {
        return x >= 0 && x < width && y >= 0 && y < width;
    }
}
