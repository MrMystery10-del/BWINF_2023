package de.nandu.boxes;

import java.awt.*;

public class SingleBox extends Box {
    @Override
    public Point[] activeLight() {
        if (!super.sensor) {
            return new Point[0];
        } else {
            return new Point[]{
                    new Point(position.x, position.y + 1)
            };
        }
    }
}
