package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MapService {
    private LocationRepository locationRepository;
    public String getMap() {
        List<Location> locations = locationRepository.findAll();
        String svg = "";
        // ????
        return svg;
    }
}
