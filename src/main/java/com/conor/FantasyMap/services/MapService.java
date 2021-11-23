package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    Map generateMap() {
        Map map = new Map();
        return map;
    }
}
