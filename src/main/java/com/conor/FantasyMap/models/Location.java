package com.conor.FantasyMap.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

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

    private static final List<Integer> cardinalDegrees = List.of(0, 45, 90, 135, 180, 225, 270, 315, 360);
    private static final List<String> cardinalValues = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW", "N");
    private static final int cardinalOffset = (360/cardinalValues.stream().distinct().toArray().length)/2;

    public void setCoordsFromOriginByVector(Location origin, String direction, Integer distance) {
        int index = cardinalValues.indexOf(direction);
        double angle = toRadians(cardinalDegrees.get(index));
        int x = (int) round(origin.getXCoord() + distance * cos(angle));
        int y = (int) round(origin.getYCoord() + distance * sin(angle));
        this.setXCoord(x);
        this.setYCoord(y);
    }

    public String calculateBearingTo(Location destination) {
        int deltaX = destination.getXCoord() - this.getXCoord();
        int deltaY = destination.getYCoord() - this.getYCoord();
        int angle = (int) toDegrees(atan2(deltaY, deltaX));
        if (angle < 0) {angle += 360;}
        int bearing = angle;
        int index = cardinalDegrees.stream().filter(i -> bearing > (i - cardinalOffset)).toArray().length - 1;
        return cardinalValues.get(index);
    }

    public Integer calculateDistanceTo(Location destination) {
        int deltaX = destination.getXCoord() - this.getXCoord();
        int deltaY = destination.getYCoord() - this.getYCoord();
        return (int) round(sqrt(pow(deltaX, 2) + pow(deltaY, 2)));
    }

    public void updateInfo(String newInfo) {
        List<String> info = this.getInfo();
        List<String> newList = concat(info.stream(), Stream.of(newInfo)).collect(toList());
        this.setInfo(newList);
    }
}
