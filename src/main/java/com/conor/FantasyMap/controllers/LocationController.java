package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.*;
import com.conor.FantasyMap.models.Point;
import com.conor.FantasyMap.presenters.Map;
import com.conor.FantasyMap.presenters.TravelLog;
import com.conor.FantasyMap.repositories.LocationRepository;
import com.conor.FantasyMap.repositories.LogEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class LocationController {
    private final LocationRepository locationRepository;
    private final LogEntryRepository logEntryRepository;

    @Transactional
    @GetMapping("/")
    public String map(Model model) {
        List<Location> locations = locationRepository.findAll();
        List<LogEntry> logEntries = logEntryRepository.findAll();
        Map map = new Map(locations, logEntries);
        model.addAttribute("map", map);
        model.addAttribute("locations", locations);
        TravelLog travelLog = new TravelLog(logEntries);
        model.addAttribute("travelLog", travelLog);
        model.addAttribute("byId", Comparator.comparing(Location::getId));
        return "map";
    }

    @Transactional
    @GetMapping("/locations")
    @ResponseBody
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Transactional
    @GetMapping("/locations/{name}")
    @ResponseBody
    public Location getLocationByName(@PathVariable String name) {
        return locationRepository.findLocationByName(name);
    }

    @Transactional
    @PostMapping(path="/locations/delete",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String deleteLocation(DeleteLocationRequest request) {
        String locationName = request.getLocationName();
        Location location = locationRepository.findLocationByName(locationName);
        try {
            if(location.isOrigin()) {
                throw new Exception("Cannot delete origin location");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        Long locationId = location.getId();
        logEntryRepository.removeDestinationRef(locationId);
        locationRepository.deleteByName(locationName);
        return "redirect:/";
    }

    @Transactional
    @GetMapping("/locations/{originName}?distanceTo={destinationName}")
    @ResponseBody
    public String getCourse(@PathVariable String originName, @PathVariable String destinationName) {
        Location origin = locationRepository.findLocationByName(originName);
        Location destination = locationRepository.findLocationByName(destinationName);
        Integer distance = origin.calculateDistanceTo(destination);
        CardinalDirection direction = origin.calculateBearingTo(destination);
        return destinationName + " is " + distance + " miles " + direction + " from " + originName;
    }

    @Transactional
    @PostMapping("/locations")
    @ResponseBody
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping(path="/locations/relative",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addRelativeLocation(NewLocationRequest request) {
        Location originLocation = locationRepository.findLocationByName(request.getOrigin());
        Location targetLocation = new Location();
        targetLocation.setName(request.getNewLocationName());
        targetLocation.setCoordsFromOriginByVector(originLocation, CardinalDirection.valueOf(request.getDirection()), request.getDistance());
        locationRepository.save(targetLocation);
        return "redirect:/";
    }

    @Transactional
    @PostMapping(path="/locations/party",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addLocationAtPartyPosition(NewLocationRequest request) {
        List<LogEntry> entries = logEntryRepository.findAll();
        IPoint partyPosition = entries != null ? LogEntry.sumPositionalDelta(entries) : new Point(0,0);
        Location newLocation = new Location();
        newLocation.setName(request.getNewLocationName());
        newLocation.setX(partyPosition.getX());
        newLocation.setY(partyPosition.getY());
        if(locationRepository.findAll().size() > 0) {
            newLocation.setOrigin(false);
        }
        locationRepository.save(newLocation);
        return "redirect:/";
    }

    @Transactional
    @PostMapping(path="/locations/{name}/info",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String addLocationInfo(UpdateInfoRequest request) {
        Location location = locationRepository.findLocationByName(request.getName());
        location.updateInfo(request.getInfo());
        locationRepository.save(location);
        return "redirect:/";
    }
}

