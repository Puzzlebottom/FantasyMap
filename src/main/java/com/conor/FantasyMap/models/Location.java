package com.conor.FantasyMap.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCoords", columnNames = {"xCoord", "yCoord"})})
@Getter
@Setter
public class Location {
    @Column(unique=true)
    private String name;
    private String info;
    private Integer xCoord;
    private Integer yCoord;
    @Id
    @GeneratedValue
    private Long id;

    public void setCoordsFromOriginByVector(Location origin, String direction, Integer distance) {
        Integer x = origin.getXCoord();
        Integer y = origin.getYCoord();
        switch (direction) {
            case "N" -> y += distance;
            case "NE" -> {
                x += distance;
                y += distance;
            }
            case "E" -> x += distance;
            case "SE" -> {
                x += distance;
                y -= distance;
            }
            case "S" -> y -= distance;
            case "SW" -> {
                x -= distance;
                y -= distance;
            }
            case "W" -> x -= distance;
            case "NW" -> {
                x -= distance;
                y += distance;
            }
        }
        this.setXCoord(x);
        this.setYCoord(y);
    }

    public String calculateBearingTo(Location destination) {
        int x1 = this.getXCoord();
        int y1 = this.getYCoord();
        int x2 = destination.getXCoord();
        int y2 = destination.getYCoord();

        int angle = (int) Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        if (angle < 0) {
            angle += 360;
        }

        Integer[] cardinalBreakpoints = new Integer[] {0, 45, 90, 135, 180, 225, 270, 315, 360};
        String[] cardinalValues = new String[] {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        int cardinalOffset = (360/Arrays.stream(cardinalValues).distinct().toArray().length)/2;
        int finalAngle = angle;
        int index = Arrays.stream(cardinalBreakpoints).filter(i -> finalAngle > (i - cardinalOffset)).toArray().length - 1;
        return cardinalValues[index];
    }

    public Integer calculateDistanceTo(Location destination) {
        Integer x1 = this.getXCoord();
        Integer x2 = destination.getXCoord();
        Integer y1 = this.getYCoord();
        Integer y2 = destination.getYCoord();
        return (int) Math.round(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 -y1)));
    }
}
