package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.*;
import com.conor.FantasyMap.presenters.Map;
import com.conor.FantasyMap.presenters.TravelLog;
import com.conor.FantasyMap.repositories.LocationRepository;
import com.conor.FantasyMap.repositories.LogEntryRepository;
import com.conor.FantasyMap.models.LogEntryFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@AllArgsConstructor
public class LocationController {
    private final LocationRepository locationRepository;
    private final LogEntryRepository logEntryRepository;

    @GetMapping("/")
    public String map(Model model) {
        List<Location> locations = locationRepository.findAll();
        List<LogEntry> logEntries = logEntryRepository.findAll();
        Map map = new Map().getMap(locations, logEntries);
        model.addAttribute("map", map);
        TravelLog travelLog = new TravelLog(logEntries);
        model.addAttribute("travelLog", travelLog);
        return "map";
    }
    @GetMapping("/locations")
    @ResponseBody
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/locations/{name}")
    @ResponseBody
    public Location getLocationByName(@PathVariable String name) {
        return locationRepository.findLocationByName(name);
    }

    @DeleteMapping("/locations/{name}")
    @ResponseBody
    public ResponseEntity<Object> deleteLocation(@PathVariable String name) {
        locationRepository.deleteLocationByName(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/locations/{originName}?distanceTo={destinationName}")
    @ResponseBody
    public String getCourse(@PathVariable String originName, @PathVariable String destinationName) {
        Location origin = locationRepository.findLocationByName(originName);
        Location destination = locationRepository.findLocationByName(destinationName);
        Integer distance = origin.calculateDistanceTo(destination);
        CardinalDirection direction = origin.calculateBearingTo(destination);
        return destinationName + " is " + distance + " miles " + direction + " from " + originName;
    }

    @PostMapping("/locations")
    @ResponseBody
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/locations/relative/{newLocationName}")
    @ResponseBody
    public ResponseEntity<Object> addRelativeLocation(@RequestBody String origin, @RequestBody String direction, @RequestBody Integer distance, @PathVariable String newLocationName) {
        Location originLocation = locationRepository.findLocationByName(origin);
        Location targetLocation = new Location();
        targetLocation.setName(newLocationName);
        targetLocation.setCoordsFromOriginByVector(originLocation, CardinalDirection.valueOf(direction), distance);
        locationRepository.save(targetLocation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/locations/{name}/info")
    @ResponseBody
    public ResponseEntity<Object> addLocationInfo(@PathVariable String name, @RequestBody String info) {
        Location location = locationRepository.findLocationByName(name);
        location.updateInfo(info);
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

}

