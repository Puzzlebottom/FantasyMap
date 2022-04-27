package com.conor.FantasyMap.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueCoords", columnNames = {"x", "y"})})
@Getter
@Setter
public class Location implements IPoint {
    @Column(unique=true)
    private String name;
    @ElementCollection
    private List<String> info;
    private double x;
    private double y;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isOrigin = true;
    private boolean isDestination = false;
    @OneToMany(mappedBy="location")
    private Set<LogEntry> logEntries;

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
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public void setX(double value) {
        this.x = value;
    }

    @Override
    public void setY(double value) {
        this.y = value;
    }
}
