package com.conor.FantasyMap.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCoords", columnNames = {"xCoord", "yCoord"})})
@Getter
@Setter
public class Location implements IPoint {
    @Column(unique=true)
    private String name;
    @ElementCollection
    private List<String> info;
    private Integer xCoord;
    private Integer yCoord;
    @Id
    @GeneratedValue
    private Long id;
    private boolean isOrigin = true;

    public void setCoordsFromOriginByVector(Location origin, CardinalDirection direction, Integer distance) {
        Point point = Point.fromOriginByVector(origin, direction, distance);
        this.setLocation(point);
        this.setOrigin(false);
    }

    public void updateInfo(String newInfo) {
        List<String> info = this.getInfo();
        List<String> newList = concat(info.stream(), Stream.of(newInfo)).collect(toList());
        this.setInfo(newList);
    }

    @Override
    public double getX() {
        return this.xCoord;
    }

    @Override
    public double getY() {
        return this.yCoord;
    }

    @Override
    public void setX(double value) {
        this.xCoord = (int) value;
    }

    @Override
    public void setY(double value) {
        this.yCoord = (int) value;
    }
}
