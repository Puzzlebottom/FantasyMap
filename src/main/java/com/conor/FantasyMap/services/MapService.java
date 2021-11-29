package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.Map;
import com.conor.FantasyMap.models.NamedPoint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.Comparator.comparing;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MapService {
    private int mapWidth = 900;
    private int mapHeight = 600;
    private int xOffset = 0;
    private int yOffset = 0;
    private double mapScale = 1;

    public Map getScaledMap(List<Location> locations) {
        centerOnOrigin(locations);
        scaleMap(locations);
        List<NamedPoint> points = locations.stream()
                .map(this::getNamedPoint)
                .collect(Collectors.toList());
        return new Map(points, mapWidth, mapHeight, mapScale);
    }

    private NamedPoint getNamedPoint(Location location) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord((int) ((mapWidth/(2 * mapScale) + xOffset + location.getXCoord()) * mapScale))
                .yCoord((int) ((mapHeight/(2 * mapScale) + yOffset - location.getYCoord()) * mapScale))
                .build();
    }

    private void centerOnOrigin(List<Location> locations) {
        if(locations.size() > 0) {
            Location origin = locations.stream()
                    .filter(Location::isOrigin)
                    .collect(Collectors.toList())
                    .get(0);
            this.xOffset = -origin.getXCoord();
            this.yOffset = origin.getYCoord();
        }
    }

    private void scaleMap(List<Location> locations) {
        int mapMargin = 50 * 2;
        Location origin = getOrigin(locations);
        Location limitEast = max(locations, comparing(Location::getXCoord));
        Location limitWest = min(locations, comparing(Location::getXCoord));
        Location limitNorth = max(locations, comparing(Location::getYCoord));
        Location limitSouth = min(locations, comparing(Location::getYCoord));
        double boundX = 2 * max(limitEast.calculateDistanceTo(origin), limitWest.calculateDistanceTo(origin));
        double boundY = 2 * max(limitNorth.calculateDistanceTo(origin), limitSouth.calculateDistanceTo(origin));
        double scaleX = boundX/(mapWidth - mapMargin);
        double scaleY = boundY/(mapHeight - mapMargin);
        this.mapScale = max(scaleX, scaleY) == 0 ? 1 : 1/max(scaleX, scaleY);
    }

    private Location getOrigin(List<Location> locations) {
        return locations.stream().filter(Location::isOrigin).collect(Collectors.toList()).get(0);
    }
}
