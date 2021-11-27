package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.NamedPoint;
import com.conor.FantasyMap.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MapService {
    public int width = 550;
    public int height = 450;

    public List<NamedPoint> getScaledMap(List<Location> locations) {
        return locations.stream()
                .map(this::getNamedPoint)
                .collect(Collectors.toList());
    }

    private NamedPoint getNamedPoint(Location location) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord(width/2 + location.getXCoord())
                .yCoord(height/2 - location.getYCoord())
                .build();
    }

}
