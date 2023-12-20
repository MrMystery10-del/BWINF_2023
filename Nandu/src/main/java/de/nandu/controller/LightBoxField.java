package de.nandu.controller;

import de.nandu.boxes.Box;
import de.nandu.boxes.SingleBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that stores light boxes in unordered way
 */
public class LightBoxField {

    private Box[] boxes;

    /**
     * Constructs a new LightBoxField object with given max count of possible boxes
     */
    public LightBoxField(int boxCount) {
        boxes = new Box[boxCount];
    }

    // A private constructor that is used by the addAllBoxes method
    private LightBoxField() {

    }

    /**
     * This method adds a box to the LightBoxField
     *
     * @return true if successful, false otherwise
     */
    public boolean addBox(Box box) {
        for (int x = 0; x < boxes.length; x++) {
            if (boxes[x] == null) {
                boxes[x] = box;
                return true;
            }
        }
        // If no slot is empty, return false
        return false;
    }

    /**
     * A factory method that returns a new LightBoxField object with all the boxes added to it
     */
    public static LightBoxField addAllBoxes(List<Box> boxList) {
        LightBoxField lightBoxField = new LightBoxField();
        Box[] boxes = new Box[boxList.size()];

        int lastIndex = 0;
        for (Box box : boxList) {
            for (int x = lastIndex; x < boxes.length; x++) {
                if (boxes[x] == null) {
                    boxes[x] = box;
                    lastIndex = x;
                    break;
                }
            }
        }
        lightBoxField.boxes = boxes;
        return lightBoxField;
    }

    public String getOutputTable() {
        StringBuilder output = new StringBuilder(getTableHead());

        List<boolean[]> combinations = getBooleanCombinations((int) getQCount());

        for (boolean[] combination : combinations) {
            boolean[] results = getLightOutput(combination, (int) getLCount());

            for (boolean sourceValue : combination) {
                output.append("| ").append(sourceValue).append(" ");
            }
            for (boolean sourceValue : results) {
                output.append("| ").append(sourceValue).append(" ");
            }
            output.append("|").append('\n');
        }

        return output.toString();
    }

    private String getTableHead() {
        StringBuilder output = new StringBuilder();

        long qCount = getQCount(), lCount = getLCount();
        for (int x = 0; x < qCount + lCount; x++) {
            output.append("| ").append(x < qCount ? "Q" : "L").append(x < qCount ? x + 1 : x + 1 - qCount).append(" ");
        }

        return output.append("|").append('\n').toString();
    }

    private boolean[] getLightOutput(boolean[] onOff, int outputBoxes) {
        // Declare and initialize the array with the specified size
        boolean[] lights = new boolean[outputBoxes];

        // Use a List to store the next light positions
        List<Point> nextLightPosition = new ArrayList<>();

        int onOffindex = 0;
        int lightsIndex = 0;
        for (Box box : boxes) {
            // Check if the box is a SingleBox and its row is 0
            if (box instanceof SingleBox) {
                if (box.getRow() == 0) {
                    // Activate the sensor with the corresponding onOff value
                    box.activateSensor(onOff[onOffindex]);

                    // Add the active light positions to the list
                    nextLightPosition.addAll(Arrays.asList(box.activeLight()));

                    onOffindex++;
                } else {
                    box.activateSensor(nextLightPosition);
                    lights[lightsIndex] = box.activeLight().length != 0;

                    lightsIndex++;
                }
            } else {
                box.activateSensor(nextLightPosition);
                nextLightPosition.addAll(Arrays.asList(box.activeLight()));
            }
        }

        Arrays.stream(boxes).forEach(Box::resetSensor);
        return lights;
    }


    private long getQCount() {
        return Arrays.stream(boxes).filter(box -> box instanceof SingleBox).filter(box -> box.getRow() == 0).count();
    }

    private long getLCount() {
        return Arrays.stream(boxes).filter(box -> box instanceof SingleBox).filter(box -> box.getRow() != 0).count();
    }

    // A helper method that converts a decimal number to a boolean array of a given length
    private static boolean[] toBooleanArray(int n, int length) {
        boolean[] array = new boolean[length];
        for (int i = 0; i < length; i++) {
            array[i] = (n & (1 << i)) != 0;
        }
        return array;
    }

    // A method that returns a list of all possible boolean combinations of a given length
    private static List<boolean[]> getBooleanCombinations(int n) {
        List<boolean[]> list = new ArrayList<>();
        // There are 2^n possible combinations
        int max = 1 << n;
        for (int i = 0; i < max; i++) {
            list.add(toBooleanArray(i, n));
        }
        return list;
    }
}
