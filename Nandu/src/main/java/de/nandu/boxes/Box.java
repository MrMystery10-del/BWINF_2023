package de.nandu.boxes;

import java.awt.*;
import java.util.List;

public abstract class Box {

    protected boolean sensor;

    protected final Point position = new Point();

    /**
     * Sets the sensor to on, on given position of the box
     */
    public void activateSensor(List<Point> position) {
        for(Point pos : position){
            if (this.position.equals(pos)) {
                sensor = true;
                break;
            }
        }
    }

    public void activateSensor(boolean activate) {
        sensor = activate;
    }

    /**
     * Sets the position of the box
     */
    public void setStartingPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public void resetSensor(){
        sensor = false;
    }

    public int getRow() {
        return position.y;
    }

    /**
     * Returns an array of points which are active with light
     */
    public abstract Point[] activeLight();
}
