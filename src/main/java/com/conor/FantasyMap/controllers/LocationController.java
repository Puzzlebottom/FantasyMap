package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
//TODO: Create Git repo, push to github
//TODO: Create new endpoint that adds a location relative to another location
//TODO: Create new endpoint that gets heading and distance between two locations
//TODO: Create new endpoint that gets location by name
//TODO: STRETCH GOAL Search Baeldung for how to implement validation for name uniqueness

public class LocationController {
    private final LocationRepository locationRepository;

    @GetMapping("/locations")
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/locations/name")
    public Location getLocationByName(@RequestParam String name) {
        return locationRepository.findLocationByName(name);
    }

    @GetMapping("/course")
    public String getCourse(@RequestParam String originName, @RequestParam String destinationName) {
        Location origin = locationRepository.findLocationByName(originName);
        Location destination = locationRepository.findLocationByName(destinationName);
        Integer distance = origin.calculateDistanceTo(destination);
        String direction = origin.calculateBearingTo(destination);
        return destinationName + " is " + distance + " miles " + direction + " from " + originName;
    }

    @PostMapping("/locations")
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/locations/relative")
    public ResponseEntity<Object> addRelativeLocation(@RequestParam String origin, @RequestParam String direction, @RequestParam Integer distance, @RequestBody Location targetLocation) {
        Location originLocation = locationRepository.findLocationByName(origin);
        targetLocation.setCoordsFromOriginByVector(originLocation, direction, distance);
        locationRepository.save(targetLocation);
        return ResponseEntity.ok().build();
    }
}

