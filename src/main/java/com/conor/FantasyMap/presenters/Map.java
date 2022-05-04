package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.IPoint;
import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.Point;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        IPoint origin = getOrigin(locations);
        IPoint partyPositionWorld = getPartyPositionWorld(log, origin);
        List<IPoint> locationsAndPartyPosition = Stream.concat(Stream.of(partyPositionWorld), locations.stream()).toList();
        double scale = getMapScale(locationsAndPartyPosition, origin);
        List<NamedPoint> points = locations.stream()
                .map(location -> getNamedPoint(location, offset, scale))
                .collect(Collectors.toList());
        this.setPartyMapCoords(getPartyPositionMap(log, offset, scale, origin));
        this.setPoints(points);
        this.setScale(scale);
        return this;
    }

    private NamedPoint getNamedPoint(Location location, Point offset, double scale) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord((int) ((WIDTH / (2 * scale) + offset.getX() + location.getX()) * scale))
                .yCoord((int) ((HEIGHT / (2 * scale) + offset.getY() - location.getY()) * scale))
                .build();
    }

    private Point centerOnOrigin(List<Location> locations) {
        Optional<Point> origin = locations.stream()
                .filter(Location::isOrigin)
                .map(l -> new Point(-l.getX(), l.getY()))
                .findFirst();

        return origin.orElse(new Point(0, 0));

    }

    private double getMapScale(List<IPoint> locations, IPoint origin) {
        if (locations.size() < 2) {
            return 1;
        }

        int mapMargin = 50 * 2;
        IPoint limitEast = max(locations, comparing(IPoint::getX));
        IPoint limitWest = min(locations, comparing(IPoint::getX));
        IPoint limitNorth = max(locations, comparing(IPoint::getY));
        IPoint limitSouth = min(locations, comparing(IPoint::getY));
        double boundX = 2 * Math.max(limitEast.calculateDistanceTo(origin), limitWest.calculateDistanceTo(origin));
        double boundY = 2 * Math.max(limitNorth.calculateDistanceTo(origin), limitSouth.calculateDistanceTo(origin));
        if(boundX < 0.1 && boundY < 0.1) {return 1;}
        double scaleX = (WIDTH - mapMargin) / boundX;
        double scaleY = (HEIGHT - mapMargin) / boundY;
        return Math.min(scaleX, scaleY);
    }

    private IPoint getOrigin(List<Location> locations) {
        Optional<Location> origin = locations.stream().filter(Location::isOrigin).findFirst();
        if (origin.isPresent()) {
            return origin.get();
        } else {
            return new Point(0, 0);
        }
    }


    private Point getPartyPositionMap(List<LogEntry> log, Point offset, double scale, IPoint origin) {
        Point partyPosition = getPartyPositionWorld(log, origin);
        int x = (int) ((WIDTH / (2 * scale) + offset.getX() + partyPosition.getX()) * scale);
        int y = (int) ((HEIGHT / (2 * scale) + offset.getY() - partyPosition.getY()) * scale);
        return new Point(x, y);
    }

    private Point getPartyPositionWorld(List<LogEntry> log, IPoint origin) {
        Point partyPosition = new Point();
        if (log.size() > 0) {
            partyPosition.setLocation(LogEntry.sumPositionalDelta(log));
        } else {
            partyPosition.setLocation(origin.getX(), origin.getY());
        }
        return partyPosition;
    }
}
