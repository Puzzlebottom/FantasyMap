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

    public List<Location> mapLocations() {
        //        String svg = "";
//       ????
        return locationRepository.findAll();
    }
}
