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

    @GetMapping("/stored-locations")
    @ResponseBody
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/stored-locations/location")
    @ResponseBody
    public Location getLocationByName(@RequestParam String name) {
        return locationRepository.findLocationByName(name);
    }

    @GetMapping("/stored-locations/location/info")
    @ResponseBody
    public List<String> getLocationInfo(@RequestParam String name) {
        Location location = locationRepository.findLocationByName(name);
        return location.getInfo();
    }

    @DeleteMapping("/stored-locations/location")
    @ResponseBody
    public ResponseEntity<Object> deleteLocation(@RequestParam String name) {
        locationRepository.deleteLocationByName(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stored-locations/navigate")
    @ResponseBody
    public String getCourse(@RequestParam String originName, @RequestParam String destinationName) {
        Location origin = locationRepository.findLocationByName(originName);
        Location destination = locationRepository.findLocationByName(destinationName);
        Integer distance = origin.calculateDistanceTo(destination);
        CardinalDirection direction = origin.calculateBearingTo(destination);
        return destinationName + " is " + distance + " miles " + direction + " from " + originName;
    }

    @PostMapping("/stored-locations")
    @ResponseBody
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stored-locations/navigate")
    @ResponseBody
    public ResponseEntity<Object> addRelativeLocation(@RequestParam String origin, @RequestParam String direction, @RequestParam Integer distance, @RequestBody Location targetLocation) {
        Location originLocation = locationRepository.findLocationByName(origin);
        targetLocation.setCoordsFromOriginByVector(originLocation, CardinalDirection.valueOf(direction), distance);
        locationRepository.save(targetLocation);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/stored-locations/location/info")
    @ResponseBody
    public ResponseEntity<Object> addLocationInfo(@RequestParam String targetLocation, @RequestBody String info) {
        Location location = locationRepository.findLocationByName(targetLocation);
        location.updateInfo(info);
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

}

