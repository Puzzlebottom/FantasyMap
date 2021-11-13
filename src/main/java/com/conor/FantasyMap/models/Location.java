package com.conor.FantasyMap.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@Entity
public class Location {
    @Getter
    @Setter
    private String name;
    @Getter
    private String info;
    @Getter
    @Setter
    private Integer xCoord;
    @Getter
    @Setter
    private Integer yCoord;
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    public void setCoordsFromOriginByVector(Location origin, String direction, Integer distance) {
        Integer originX = origin.xCoord;
        Integer originY = origin.yCoord;
        Integer x = originX;
        Integer y = originY;
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
}
