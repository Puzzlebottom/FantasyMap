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
    // TODO: Rename to GET /locations
    @GetMapping("/stored-locations")
    @ResponseBody
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /* TODO: Rename to GET /locations/{name}
        Rationale: Query params (@RequestParam, example.com/foo?some) should be used for querying/filtering results,
        not for specifying a resource. The URI should specify the resource.

        Today, you GET /stored-locations/location?name=Bastion
        After this change it becomes GET /locations/Bastion
        This makes sense, right? Bastion is a resource, we should be able to access it with just a URI.

        More:
        https://stackoverflow.com/questions/30967822/when-do-i-use-path-params-vs-query-params-in-a-restful-api
     */
    @GetMapping("/stored-locations/location")
    @ResponseBody
    public Location getLocationByName(@RequestParam String name) {
        return locationRepository.findLocationByName(name);
    }

    // TODO: Delete; this duplicates the above since info is returned in the other endpoint as well
    @GetMapping("/stored-locations/location/info")
    @ResponseBody
    public List<String> getLocationInfo(@RequestParam String name) {
        Location location = locationRepository.findLocationByName(name);
        return location.getInfo();
    }

    // TODO: Rename to DELETE /locations/{name} (path instead of query params, same as above)
    @DeleteMapping("/stored-locations/location")
    @ResponseBody
    public ResponseEntity<Object> deleteLocation(@RequestParam String name) {
        locationRepository.deleteLocationByName(name);
        return ResponseEntity.ok().build();
    }

    /* TODO: Rename to GET /locations/{originName}?distanceTo={destinationName}
        Rationale: this is a query on the origin location, so we should use a query param for the query part
        -- but not the primary resource which is the origin location
     */
    @GetMapping("/stored-locations/navigate")
    @ResponseBody
    public String getCourse(@RequestParam String originName, @RequestParam String destinationName) {
        Location origin = locationRepository.findLocationByName(originName);
        Location destination = locationRepository.findLocationByName(destinationName);
        Integer distance = origin.calculateDistanceTo(destination);
        CardinalDirection direction = origin.calculateBearingTo(destination);
        return destinationName + " is " + distance + " miles " + direction + " from " + originName;
    }

    // TODO: Rename to POST /locations
    @PostMapping("/stored-locations")
    @ResponseBody
    public ResponseEntity<Object> addLocation(@RequestBody Location location) {
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

    /* TODO:

        1. Refactor to a request body (example of this immediately above)
        Rationale: query params are for querying/filtering, not for persisting data. Request bodies are for persisting data.

        2. Rename to POST /locations/relative or /locations/{name}/relative -- Ideally, this should be POST /locations and spring should differentiate
        between the two by the different request bodies. I don't think spring can do this, so having a different
        URI is a kludge.
    */
    @PostMapping("/stored-locations/navigate")
    @ResponseBody
    public ResponseEntity<Object> addRelativeLocation(@RequestParam String origin, @RequestParam String direction, @RequestParam Integer distance, @RequestBody Location targetLocation) {
        Location originLocation = locationRepository.findLocationByName(origin);
        targetLocation.setCoordsFromOriginByVector(originLocation, CardinalDirection.valueOf(direction), distance);
        locationRepository.save(targetLocation);
        return ResponseEntity.ok().build();
    }


    // TODO: Rename to POST /locations/{name}/info
    @PostMapping("/stored-locations/location/info")
    @ResponseBody
    public ResponseEntity<Object> addLocationInfo(@RequestParam String targetLocation, @RequestBody String info) {
        Location location = locationRepository.findLocationByName(targetLocation);
        location.updateInfo(info);
        locationRepository.save(location);
        return ResponseEntity.ok().build();
    }

}

