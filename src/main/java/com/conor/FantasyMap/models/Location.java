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

    public String calculateBearingTo(Location destination) {
        int x1 = this.getXCoord();
        int x2 = destination.getXCoord();
        int y1 = this.getYCoord();
        int y2 = destination.getYCoord();
        double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
        if(angle < 0){
            angle += 360;
        }

        if ((int) angle >= 22.5 && angle < 67.5) {
            return "NE";
        } else if ((int) angle >= 67.5 && angle < 112.5) {
            return "E";
        } else if ((int) angle >= 112.5 && angle < 157.5) {
            return "SE";
        } else if ((int) angle >= 157.5 && angle < 202.5) {
            return "S";
        } else if ((int) angle >= 202.5 && angle < 247.5) {
            return "SW";
        } else if ((int) angle >= 247.5 && angle < 292.5) {
            return "W";
        } else if ((int) angle >= 292.5 && angle < 337.5) {
            return "NW";
        } else return "N";
    }

    public Integer calculateDistanceTo(Location destination) {
        Integer x1 = this.getXCoord();
        Integer x2 = destination.getXCoord();
        Integer y1 = this.getYCoord();
        Integer y2 = destination.getYCoord();
        long distance = Math.round(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 -y1)));
        return (int) distance;
    }
}
