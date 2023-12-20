package de.nandu.boxes;

import java.awt.*;

/**
 * Lights always both unless the sensor gets light up
 */
public class RedBox extends DoubleBox {

    private boolean noSensor;

    public RedBox(boolean noSensor) {
        this.noSensor = noSensor;
    }

    @Override
    public Point[] activeLight() {
        if (super.sensor && !noSensor || noSensor && super.additionalSensor) {
            return new Point[0];
        } else {
            Point leftSide = super.position;
            Point rightSide = super.additionalPosition;
            return new Point[]{
                    new Point(leftSide.x, leftSide.y + 1),
                    new Point(rightSide.x, rightSide.y + 1)
            };
        }
    }
}
