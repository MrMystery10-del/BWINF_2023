package de.nandu.controller;

import de.nandu.boxes.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskReader {

    /**
     * This method transforms a file into a LightBoxField object
     *
     * @apiNote This method reads the lines from file and converts to String which is parsed
     * to transformInformation(String information)
     */
    public static LightBoxField transformInformation(Path informationPath) {
        try {
            return transformInformation(Files.readAllLines(informationPath, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + informationPath, exception);
        }
    }

    /**
     * This method transforms information from a given String into a LightBoxField object
     *
     * @apiNote Make sure the given String is valid, this method does not check for its correctness
     * not valid String can cause unexpected issues
     */
    public static LightBoxField transformInformation(List<String> information) {
        List<Box> boxes = new ArrayList<>(information.size() * information.get(0).length());

        for (int i = 0; i < information.size(); i++) {
            information.set(i, information.get(i).replaceAll("\\s|\\d", ""));
            for (int j = 0; j < information.get(i).length(); j++) {
                String line = information.get(i);
                Box box = switch (line.charAt(j)) {
                    case 'W' -> new WhiteBox();
                    case 'R' -> new RedBox(false);
                    case 'r' -> new RedBox(true);
                    case 'B' -> new BlueBox();
                    case 'Q', 'L' -> new SingleBox();
                    default -> null; // Ignore other characters
                };
                // If a box was created, set its position and add it to the list
                if (box != null) {
                    box.setStartingPosition(j, i);
                    boxes.add(box);

                    if (!(box instanceof SingleBox)) {
                        j++;
                    }
                }
            }
        }
        return LightBoxField.addAllBoxes(boxes);
    }
}
