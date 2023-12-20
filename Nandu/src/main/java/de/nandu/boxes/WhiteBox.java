package de.nandu.boxes;

import java.awt.*;

/**
 * Lights always both unless both sensors get light up
 */
public class WhiteBox extends DoubleBox {

    @Override
    public Point[] activeLight() {
        if (super.sensor && super.additionalSensor) {
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
