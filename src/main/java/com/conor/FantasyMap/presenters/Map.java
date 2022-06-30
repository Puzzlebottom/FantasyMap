package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.IPoint;
import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.Point;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHull2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.ConvexHullGenerator2D;
import org.apache.commons.math3.geometry.euclidean.twod.hull.MonotoneChain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Map {
    private final List<Location> locations;
    private final List<LogEntry> log;
    @Getter
    private final int WIDTH = 900;
    @Getter
    private final int HEIGHT = 600;


    public Map(List<Location> locations, List<LogEntry> log) {
        this.locations = locations;
        this.log = log;
    }

    public List<NamedPoint> getPoints() {
        double scale = getScale();
        Point offset = centerOnOrigin();
        List<NamedPoint> points = locations.stream()
                .map(location -> getNamedPoint(location, offset, scale))
                .collect(toList());
        return points;
    }

    public Point getPartyMapCoords() {
        Point offset = centerOnOrigin();
        double scale = getScale();
        Point partyPosition = getPartyPositionWorld();
        int x = (int) ((WIDTH / (2 * scale) + offset.getX() + partyPosition.getX()) * scale);
        int y = (int) ((HEIGHT / (2 * scale) + offset.getY() - partyPosition.getY()) * scale);
        return new Point(x, y);
    }

    public double getScale() {
        IPoint origin = getOrigin();
        IPoint partyPositionWorld = getPartyPositionWorld();
        List<IPoint> locationsAndPartyPosition = Stream.concat(Stream.of(partyPositionWorld), locations.stream()).toList();

        if (locationsAndPartyPosition.size() < 2) {
            return 1;
        }

        int mapMargin = 50 * 2;
        IPoint limitEast = max(locationsAndPartyPosition, comparing(IPoint::getX));
        IPoint limitWest = min(locationsAndPartyPosition, comparing(IPoint::getX));
        IPoint limitNorth = max(locationsAndPartyPosition, comparing(IPoint::getY));
        IPoint limitSouth = min(locationsAndPartyPosition, comparing(IPoint::getY));
        double boundX = 2 * Math.max(limitEast.calculateDistanceTo(origin), limitWest.calculateDistanceTo(origin));
        double boundY = 2 * Math.max(limitNorth.calculateDistanceTo(origin), limitSouth.calculateDistanceTo(origin));
        if(boundX < 0.1 && boundY < 0.1) {return 1;}
        double scaleX = (WIDTH - mapMargin) / boundX;
        double scaleY = (HEIGHT - mapMargin) / boundY;
        return Math.min(scaleX, scaleY);
    }

    public String getCurrentDestinationName() {
        return LogEntry.getCurrentDestination(log).map(Location::getName).orElse(null);
    }

    public String getFogOfWarPolygonPoints() {
        Stream<NamedPoint> locations = getPoints().stream();
        Stream<IPoint> party = Stream.of(getPartyMapCoords());
        Stream<IPoint> partyAndLocations = Stream.concat(party, locations);
        List<Vector2D> locationVectors = partyAndLocations
                .map(l -> new Vector2D(l.getX(), l.getY()))
                .collect(toList());

        ConvexHullGenerator2D convexHullGenerator = new MonotoneChain(true);
        ConvexHull2D convexHull = convexHullGenerator.generate(locationVectors);

        return Arrays.stream(convexHull.getVertices())
                .map(vertex -> "%s,%s".formatted(vertex.getX(), vertex.getY()))
                .collect(joining(" "));
    }

    private NamedPoint getNamedPoint(Location location, Point offset, double scale) {
        return NamedPoint.builder()
                .name(location.getName())
                .x((int) ((WIDTH / (2 * scale) + offset.getX() + location.getX()) * scale))
                .y((int) ((HEIGHT / (2 * scale) + offset.getY() - location.getY()) * scale))
                .build();
    }

    private Point centerOnOrigin() {
        Optional<Point> origin = locations.stream()
                .filter(Location::isOrigin)
                .map(l -> new Point(-l.getX(), l.getY()))
                .findFirst();

        return origin.orElse(new Point(0, 0));

    }

    private IPoint getOrigin() {
        Optional<Location> origin = locations.stream().filter(Location::isOrigin).findFirst();
        if (origin.isPresent()) {
            return origin.get();
        } else {
            return new Point(0, 0);
        }
    }

    private Point getPartyPositionWorld() {
        IPoint origin = getOrigin();
        Point partyPosition = new Point();
        if (log.size() > 0) {
            partyPosition.setLocation(LogEntry.sumPositionalDelta(log));
        } else {
            partyPosition.setLocation(origin.getX(), origin.getY());
        }
        return partyPosition;
    }
}
