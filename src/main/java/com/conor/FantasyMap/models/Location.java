package com.conor.FantasyMap.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCoords", columnNames = {"xCoord", "yCoord"})})
@Getter
@Setter
public class Location {
    @Column(unique=true)
    private String name;
    @ElementCollection
    private List<String> info;
    private Integer xCoord;
    private Integer yCoord;
    @Id
    @GeneratedValue
    private Long id;

    private static List<Integer> cardinalDegrees = List.of(0, 45, 90, 135, 180, 225, 270, 315, 360);
    private static List<String> cardinalValues = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N");

    public void setCoordsFromOriginByVector(Location origin, String direction, Integer distance) {
        int index = cardinalValues.indexOf(direction);
        double angle = Math.toRadians(cardinalDegrees.get(index));
        int destinationX = (int) Math.round(origin.getXCoord() + distance * Math.cos(angle));
        int destinationY = (int) Math.round(origin.getYCoord() + distance * Math.sin(angle));
        this.setXCoord(destinationX);
        this.setYCoord(destinationY);
    }

    public String calculateBearingTo(Location destination) {
        int x1 = this.getXCoord();
        int y1 = this.getYCoord();
        int x2 = destination.getXCoord();
        int y2 = destination.getYCoord();

        int angle = (int) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        if (angle < 0) {
            angle += 360;
        }

        int cardinalOffset = (360/cardinalValues.stream().distinct().toArray().length)/2;
        int finalAngle = angle;
        int index = cardinalDegrees.stream().filter(i -> finalAngle > (i - cardinalOffset)).toArray().length - 1;
        return cardinalValues.get(index);
    }

    public Integer calculateDistanceTo(Location destination) {
        Integer x1 = this.getXCoord();
        Integer x2 = destination.getXCoord();
        Integer y1 = this.getYCoord();
        Integer y2 = destination.getYCoord();
        return (int) Math.round(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 -y1)));
    }
}
