package de.nandu.boxes;

import java.awt.*;
import java.util.List;

/**
 * Represents a double box which contains two positions including, two sensors and two lights
 */
public abstract class DoubleBox extends Box {

    protected boolean additionalSensor;
    protected final Point additionalPosition = new Point();

    @Override
    public void activateSensor(List<Point> position) {
        for (Point pos : position) {
            if (super.position.equals(pos)) {
                super.sensor = true;
            } else if (additionalPosition.equals(pos)) {
                additionalSensor = true;
            }
        }
    }

    @Override
    public void setStartingPosition(int x, int y) {
        super.position.x = x;
        super.position.y = y;
        additionalPosition.x = x + 1;
        additionalPosition.y = y;
    }

    @Override
    public void resetSensor() {
        super.resetSensor();
        additionalSensor = false;
    }
}
