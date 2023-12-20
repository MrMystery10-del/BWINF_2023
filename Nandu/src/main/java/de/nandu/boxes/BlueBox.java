package de.nandu.boxes;

import java.awt.*;

/**
 * Only lights where the sensor is lighted up
 */
public class BlueBox extends DoubleBox {

    @Override
    public Point[] activeLight() {
        Point[] activeLights = new Point[2];
        activeLights[0] = super.sensor ? new Point(super.position.x, super.position.y + 1) : null;
        activeLights[1] = super.additionalSensor ? new Point(super.additionalPosition.x, super.additionalPosition.y + 1) : null;
        return activeLights;
    }
}
