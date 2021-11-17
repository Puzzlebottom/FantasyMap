package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor

//TODO: Make endpoints Restful;

public class LocationController {
    private final LocationRepository locationRepository;

    @GetMapping("/stored-locations")
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/stored-locations/location")
    public Location getLocationByName(@RequestParam String name) {
        return locationRepository.findLocationByName(name);
    }

    @GetMapping("/stored-locations/location/info")
    public List<String> getLocationInfo(@RequestParam String name) {
        Location location = locationRepository.findLocationByName(name);
        return location.getInfo();
    }

    @DeleteMapping("/stored-locations/location")
    public ResponseEntity<Object> deleteLocation(@RequestParam String name) {
        locationRepository.deleteLocationByName(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stored-locations/navigate")
    public String getCourse(@RequestParam String originName, @RequestParam String destinationName) {
        Location origin = locationRepository.findLocationByName(originName);
        Location destination = locationRepository.findLocationByName(destinationName);
        Integer distance = origin.calculateDistanceTo(destination);
        String direction = origin.calculateBearingTo(destination);
        return destinationName + " is " + distance + " miles " + direction + " from " + originName;
    }

    @PostMapping("/stored-locations")
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stored-locations/navigate")
    public ResponseEntity<Object> addRelativeLocation(@RequestParam String origin, @RequestParam String direction, @RequestParam Integer distance, @RequestBody Location targetLocation) {
        Location originLocation = locationRepository.findLocationByName(origin);
        targetLocation.setCoordsFromOriginByVector(originLocation, direction, distance);
        locationRepository.save(targetLocation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stored-locations/location/info")
    public ResponseEntity<Object> addLocationInfo(@RequestParam String targetLocation, @RequestBody String info) {
        Location location = locationRepository.findLocationByName(targetLocation);
        location.updateInfo(info);
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }
}

