package com.conor.FantasyMap.models;

import lombok.*;

import static java.lang.Math.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Point implements IPoint {
    private double x;
    private double y;

    public static Point fromOriginByVector(IPoint origin, CardinalDirection direction, Integer distance) {
        double angle = toRadians(direction.toAngle());
        int x = (int) round(origin.getX() + distance * sin(angle));
        int y = (int) round(origin.getY() + distance * cos(angle));
        return new Point(x, y);
    }
}
