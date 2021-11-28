package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.NamedPoint;
import com.conor.FantasyMap.models.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MapService {
    private int mapWidth = 900;
    private int mapHeight = 600;
    private double mapScale = 0.5;

    public Map getScaledMap(List<Location> locations) {
        List<NamedPoint> points = locations.stream()
                .map(this::getNamedPoint)
                .collect(Collectors.toList());
        Map map = new Map(points, mapWidth, mapHeight, mapScale);
        return map;
    }

    private NamedPoint getNamedPoint(Location location) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord((int) ((mapWidth/2 + location.getXCoord()) * mapScale))
                .yCoord((int) ((mapHeight/2 - location.getYCoord()) * mapScale))
                .isCentered(location.isOrigin())
                .build();
    }

}
