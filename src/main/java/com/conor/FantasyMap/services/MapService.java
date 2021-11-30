package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.Map;
import com.conor.FantasyMap.models.NamedPoint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

@Service
public class MapService {
    private final int MAP_WIDTH = 900;
    private final int MAP_HEIGHT = 600;

    public Map getMap(List<Location> locations) {
        Point offset = centerOnOrigin(locations);
        double scale = getMapScale(locations);
        List<NamedPoint> points = locations.stream()
                .map(location -> getNamedPoint(location, offset, scale))
                .collect(Collectors.toList());
        return new Map(points, MAP_WIDTH, MAP_HEIGHT, scale);
    }

    private NamedPoint getNamedPoint(Location location, Point offset, double scale) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord((int) ((MAP_WIDTH /(2 * scale) + offset.getX() + location.getXCoord()) * scale))
                .yCoord((int) ((MAP_HEIGHT /(2 * scale) + offset.getY() - location.getYCoord()) * scale))
                .build();
    }

    private Point centerOnOrigin(List<Location> locations) {
        if(locations.size() > 0) {
            Location origin = locations.stream()
                    .filter(Location::isOrigin)
                    .collect(Collectors.toList())
                    .get(0);
            return new Point(-origin.getXCoord(), origin.getYCoord());
        }
        return new Point(0, 0);
    }

    private double getMapScale(List<Location> locations) {
        int mapMargin = 50 * 2;
        Location origin = getOrigin(locations);
        Location limitEast = max(locations, comparing(Location::getXCoord));
        Location limitWest = min(locations, comparing(Location::getXCoord));
        Location limitNorth = max(locations, comparing(Location::getYCoord));
        Location limitSouth = min(locations, comparing(Location::getYCoord));
        double boundX = 2 * max(limitEast.calculateDistanceTo(origin), limitWest.calculateDistanceTo(origin));
        double boundY = 2 * max(limitNorth.calculateDistanceTo(origin), limitSouth.calculateDistanceTo(origin));
        if(boundX == 0 && boundY == 0) {
            return 1;
        }
        double scaleX = (MAP_WIDTH - mapMargin)/boundX;
        double scaleY = (MAP_HEIGHT - mapMargin)/boundY;
        return Math.min(scaleX, scaleY);
    }

    private Location getOrigin(List<Location> locations) {
        return locations.stream().filter(Location::isOrigin).collect(Collectors.toList()).get(0);
    }
}
