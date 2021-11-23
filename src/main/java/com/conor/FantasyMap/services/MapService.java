package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.NamedPoint;
import com.conor.FantasyMap.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MapService {
    private LocationRepository locationRepository;

    public class Map {
        public int scale = 1;
        public int xOffset = this.scale * this.width/2;
        public int yOffset = this.scale * this.height/(-2);
        public int height = 600;
        public int width = 600;
        public List<Location> locations = locationRepository.findAll();

    }

    List<NamedPoint> getScaledMap(int width, int height) {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .map(this::getNamedPoint)
                .map(point -> point.setCoords(width/2, height/2))
                .collect(Collectors.toList());
    }

    private NamedPoint getNamedPoint(Location location) {
        return NamedPoint.builder()
                .name(location.getName())
                .xCoord(location.getXCoord())
                .yCoord(location.getYCoord())
                .build();
    }

}
