package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.Point;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

@Getter
@Setter
public class Map {
    private List<NamedPoint> points;
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    private double scale;
    private Point partyMapCoords;

    public Map getMap(List<Location> locations, List<LogEntry> log) {
        Point offset = centerOnOrigin(locations);
        double scale = getMapScale(locations);
        List<NamedPoint> points = locations.stream()
                .map(location -> getNamedPoint(location, offset, scale))
                .collect(Collectors.toList());
        this.setPartyMapCoords(locateParty(log, offset, scale, getOrigin(locations)));
        this.setPoints(points);
        this.setScale(scale);
        return this;
    }

    private NamedPoint getNamedPoint(Location location, Point offset, double scale) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord((int) ((WIDTH /(2 * scale) + offset.getX() + location.getX()) * scale))
                .yCoord((int) ((HEIGHT /(2 * scale) + offset.getY() - location.getYCoord()) * scale))
                .build();
    }

    private Point centerOnOrigin(List<Location> locations) {
        if(locations.size() > 0) {
            Location origin = locations.stream()
                    .filter(Location::isOrigin)
                    .collect(Collectors.toList())
                    .get(0);
            return new Point(-origin.getX(), origin.getYCoord());
        }
        return new Point(0, 0);
    }

    private double getMapScale(List<Location> locations) {
        int mapMargin = 50 * 2;
        Location origin = getOrigin(locations);
        Location limitEast = max(locations, comparing(Location::getX));
        Location limitWest = min(locations, comparing(Location::getX));
        Location limitNorth = max(locations, comparing(Location::getYCoord));
        Location limitSouth = min(locations, comparing(Location::getYCoord));
        double boundX = 2 * Math.max(limitEast.calculateDistanceTo(origin), limitWest.calculateDistanceTo(origin));
        double boundY = 2 * Math.max(limitNorth.calculateDistanceTo(origin), limitSouth.calculateDistanceTo(origin));
        if(boundX == 0 && boundY == 0) {
            return 1;
        }
        double scaleX = (WIDTH - mapMargin)/boundX;
        double scaleY = (HEIGHT - mapMargin)/boundY;
        return Math.min(scaleX, scaleY);
    }

    private Location getOrigin(List<Location> locations) {
        return locations.stream().filter(Location::isOrigin).collect(Collectors.toList()).get(0);
    }


    private Point locateParty(List<LogEntry> log, Point offset, double scale, Location origin) {
        Point partyPosition = new Point();
        if(log.size() > 0) {
            partyPosition.setLocation(LogEntry.sumPositionalDelta(log));
        } else {
            partyPosition.setLocation(origin.getX(), origin.getYCoord());
        }
        int x = (int) ((WIDTH /(2 * scale) + offset.getX() + partyPosition.getX()) * scale);
        int y = (int) ((HEIGHT /(2 * scale) + offset.getY() - partyPosition.getY()) * scale);
        return new Point(x, y);
    }
}
