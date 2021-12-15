package com.conor.FantasyMap.models;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public interface IPoint {
    double getX();
    double getY();
    void setX(double value);
    void setY(double value);

    default void setLocation(double x, double y) {
        setX(x);
        setY(y);
    }

    default void setLocation(IPoint point) {
        setLocation(point.getX(), point.getY());
    }

    default CardinalDirection calculateBearingTo(IPoint destination) {
        double deltaX = destination.getX() - this.getX();
        double deltaY = destination.getY() - this.getY();
        int angle = (int) toDegrees(atan2(deltaX, deltaY));
        return CardinalDirection.ofAngle(angle);
    }



    default int calculateDistanceTo(IPoint other) {
        double deltaX = other.getX() - this.getX();
        double deltaY = other.getY() - this.getY();
        return (int) round(sqrt(pow(deltaX, 2) + pow(deltaY, 2)));
    }
}
