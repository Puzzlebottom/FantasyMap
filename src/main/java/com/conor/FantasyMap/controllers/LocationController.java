package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.*;
import com.conor.FantasyMap.repositories.LocationRepository;
import com.conor.FantasyMap.repositories.LogEntryRepository;
import com.conor.FantasyMap.services.TravelLog;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
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
        List<LogEntry> log = logEntryRepository.findAll();
        Map map = new Map().getMap(locations, log);
        model.addAttribute("map", map);
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

    @PostMapping(path="/log-entries/free",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String moveFree(MoveRequest request) {
        LogEntry logEntry = TravelLog.createLogEntryByCourse(request.getDirection(), request.getDeltaHours());
        logEntryRepository.save(logEntry);
        return "redirect:/";
    }

    @PostMapping(path="/log-entries/destination",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String moveTo(MoveToLocationRequest request) {
        Location destination = locationRepository.findLocationByName(request.getDestinationName());
        List<LogEntry> logs = logEntryRepository.findAll();
        Point partyPosition = TravelLog.sumPositionalDelta(logs);
        LogEntry logEntry = TravelLog.createLogEntryByDestination(partyPosition, destination, request.getDeltaHours());
        logEntryRepository.save(logEntry);
        return "redirect:/";
    }
}

