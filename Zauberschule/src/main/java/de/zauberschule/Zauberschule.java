package de.zauberschule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;

public class Zauberschule {

    private final Node[][][] schoolPlan;
    private final Stack<Node> path;
    private Node start, end;

    public Zauberschule(String filePath) {
        List<String> lines = readFile(Path.of(filePath));

        schoolPlan = new Node[2][lines.size() / 2][lines.get(0).length()];

        readNodes(lines);

        path = getCheapestPathFromAToB();
    }

    public String writePath() {
        StringBuilder builder = new StringBuilder("From point A, ");

        Node currentNode = start;
        int dx, dy;
        int counter = 0;
        int time = 0;
        String direction = "";
        while (path.iterator().hasNext()) {
            dx = currentNode.getX();
            dy = currentNode.getY();

            currentNode = path.pop();

            dx -= currentNode.getX();
            dy -= currentNode.getY();

            if (dx == 0 && dy == 0) time += 3;
            else time++;

            // check if the direction has changed or the path is empty
            if (!direction.equals(getDirection(dx, dy)) || path.isEmpty()) {
                // if the counter is more than 1, append it to the instruction
                if (counter > 1) {
                    // change the order of the words here
                    builder.append("move " + counter + " times ");
                }
                // append the current direction to the instruction
                builder.append(direction);
                // reset the counter and the direction
                counter = 0;
                direction = getDirection(dx, dy);
            }
            // increment the counter
            counter++;
        }
        // append the final direction and counter before the end message
        if (counter > 1) {
            builder.append("move " + counter + " times ");
        }
        builder.append(direction);
        builder.append("now you reached point B!").append(" It did take you " + time + " seconds");
        return builder.toString();
    }


    // a helper method to get the direction string from the dx and dy values
    private String getDirection(int dx, int dy) {
        if (dx == 1) {
            return "to left, ";
        } else if (dx == -1) {
            return "to right, ";
        } else if (dy == 1) {
            return "up, ";
        } else if (dy == -1) {
            return "down, ";
        } else {
            return "switch floor, ";
        }
    }


    private Stack<Node> getCheapestPathFromAToB() {
        return A_Star.getPath(schoolPlan, start, end);
    }

    private List<String> readFile(Path filePath) {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void readNodes(List<String> lines) {
        boolean secondFloor = false;

        for (int y = 0; y < lines.size(); y++) {
            if (lines.get(y).isBlank()) {
                secondFloor = true;
                continue;
            }
            for (int x = 0; x < lines.get(y).length(); x++) {
                switch (lines.get(y).charAt(x)) {
                    case '#', '.' ->
                            schoolPlan[secondFloor ? 1 : 0][secondFloor ? y - lines.size() / 2 - 1 : y][x] = new Node(x, secondFloor ? y - lines.size() / 2 - 1 : y,
                                    lines.get(y).charAt(x) == '#', secondFloor);
                    case 'A', 'B' -> {
                        schoolPlan[0][y][x] = new Node(x, y, false, false);
                        if (lines.get(y).charAt(x) == 'A')
                            start = schoolPlan[0][y][x];
                        else end = schoolPlan[0][y][x];
                    }
                }
            }
        }
    }
}
