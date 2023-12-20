package de.zauberschule;

import java.util.*;
import java.util.stream.Collectors;

public class A_Star {

    // A method that returns a stack of nodes that represents the path from the start point to the end point in a grid
    public static Stack<Node> getPath(Node[][][] nodes, Node start, Node end) {
        // A list of nodes that are currently being considered for the path
        List<Node> currentNodes = new ArrayList<>();

        // Add the node at the start point to the current nodes list
        currentNodes.add(start);
        currentNodes.get(0).setVisited(true);

        calculateNodes(nodes, start, end);

        // Loop until the first node in the current nodes list is the end node
        while (!currentNodes.contains(end)) {
            // Get the cheapest neighbors
            List<Node> neighbors = getCheapestNeighbors(Arrays.stream(nodes).flatMap(Arrays::stream).flatMap(Arrays::stream)
                    .filter(Node::isVisited).toList(), nodes, end);
            neighbors.forEach(node -> node.setVisited(true));
            //currentNodes.forEach(node -> System.out.println(node.getX() + " " + node.getY() + " " + node.isSecondFloor() + " cost: " + node.getTotalCost()));
            //System.out.println("current^");
            //neighbors.forEach(node -> System.out.println(node.getX() + " " + node.getY() + " " + node.isSecondFloor() + " cost: " + node.getTotalCost()));
            //System.out.println("neigh^");

            // If the first neighbor is the end node, clear the current nodes list and add that neighbor to it
            if (neighbors.contains(end)) {
                currentNodes.clear();
                currentNodes.add(end);
                break;
            } else {
                currentNodes.addAll(neighbors);
            }
        }

        Stack<Node> path = new Stack<>();
        // Loop until the first node in the current nodes list is the start node
        while (currentNodes.get(0) != start) {
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
    private static List<Node> getCheapestNeighbors(List<Node> currentNodes, Node[][][] nodes, Node end) {
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
                if (inBounds(x, y, nodes[0].length, nodes[0][0].length)) {
                    // Get the node from the matrix
                    Node node = nodes[currentNode.isSecondFloor() ? 1 : 0][y][x];

                    // If the node is not an obstacle and not visited, add it to the neighbors list and set its properties
                    if (!node.isObstacle() && !node.isVisited()) {
                        node.setChild(currentNode);
                        neighbors.add(node);
                    }
                }
            }
            Node node = nodes[currentNode.isSecondFloor() ? 0 : 1][currentNode.getY()][currentNode.getX()];

            // If the node is not an obstacle and not visited, add it to the neighbors list and set its properties
            if (!node.isObstacle() && !node.isVisited()) {
                node.setChild(currentNode);
                neighbors.add(node);
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
        Comparator<Node> nodeComparator = Comparator.comparingInt(Node::getTotalCost).thenComparingInt(Node::getOriginDistance);
        //neighbors.forEach(node -> System.out.println(node.getX() + " " + node.getY() + " " + node.isSecondFloor() + " cost: " + node.getTotalCost()));
        Optional<Node> minNode = neighbors.stream().min(nodeComparator);
        //System.out.println(minNode.get().getX() + " " + minNode.get().getY() + " " + minNode.get().isSecondFloor());
        if (minNode.isPresent()) {
            // Get the minimum cost and origin distance from the minNode
            int lowestCost = minNode.get().getTotalCost();
            int lowestOriginDistance = minNode.get().getOriginDistance();
            // Return a list of nodes that match the minimum values
            return neighbors.stream()
                    .filter(node -> node.getTotalCost() == lowestCost && node.getOriginDistance() == lowestOriginDistance)
                    .collect(Collectors.toList());
        } else {
            // Return an empty list if the input list is empty
            return Collections.emptyList();
        }
    }


    // A method that returns a two-dimensional array of nodes that are calculated from a grid, a start point, and an end point
    private static void calculateNodes(Node[][][] nodes, Node start, Node end) {
        for (int floor = 0; floor < 2; floor++)
            for (int y = 0; y < nodes[0].length; y++) {
                for (int x = 0; x < nodes[0][0].length; x++) {
                    // Calculate the distance of the node from the start point
                    int originDistance = Math.abs(start.getX() - x) + Math.abs(start.getY() - y);
                    // Calculate the distance of the node from the end point
                    int goalDistance = Math.abs(end.getX() - x) + Math.abs(end.getY() - y);

                    // Create a new node with the calculated parameters and assign it to the array
                    nodes[floor][y][x].setOriginDistance(originDistance);
                    nodes[floor][y][x].setTotalCost(goalDistance + (nodes[floor][y][x].isSecondFloor() ? 4 : 0));
                }
            }
    }


    private static boolean inBounds(int x, int y, int height, int width) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}

