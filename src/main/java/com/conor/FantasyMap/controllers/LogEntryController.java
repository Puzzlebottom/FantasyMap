package com.conor.FantasyMap.controllers;


import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.LogEntryFactory;
import com.conor.FantasyMap.models.Point;
import com.conor.FantasyMap.repositories.LocationRepository;
import com.conor.FantasyMap.repositories.LogEntryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class LogEntryController {
    private final LogEntryRepository logEntryRepository;
    private final LocationRepository locationRepository;

    @Transactional
    @PostMapping(path="/log-entries/free",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String moveFree(MoveRequest request) {
        LogEntry logEntry = LogEntryFactory.createLogEntryByCourse(request.getDirection(), request.getDeltaHours());
        logEntryRepository.save(logEntry);
        return "redirect:/";
    }

    @Transactional
    @PostMapping(path="/log-entries/destination",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String moveTo(MoveToLocationRequest request) {
        Location destination = locationRepository.findLocationByName(request.getDestinationName());
        List<LogEntry> logs = logEntryRepository.findAll();
        Point partyPosition = LogEntry.sumPositionalDelta(logs);
        LogEntry logEntry = LogEntryFactory.createLogEntryByDestination(partyPosition, destination, request.getDeltaHours());
        logEntryRepository.save(logEntry);
        return "redirect:/";
    }

    @Transactional
    @PostMapping(path="/log-entries/rest",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String restInPlace(RestRequest request) {
        LogEntry logEntry = LogEntryFactory.createLogEntryForRest(request.getDeltaHours());
        logEntryRepository.save(logEntry);
        return "redirect:/";
    }

    @Transactional
    @PostMapping(path="/log-entries/teleport",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String teleportToDestination(TeleportRequest request) {
        Location destination = locationRepository.findLocationByName(request.getDestinationName());
        List<LogEntry> logs = logEntryRepository.findAll();
        Point partyPosition = LogEntry.sumPositionalDelta(logs);
        LogEntry logEntry = LogEntryFactory.createLogEntryForTeleport(partyPosition, destination);
        logEntryRepository.save(logEntry);
        return "redirect:/";
    }

    @Transactional
    @PostMapping(path="/log-entries/delete-top-entry")
    public String undoMove() {
        LogEntry topEntry = logEntryRepository.findFirstByOrderByIdDesc();
        if (topEntry != null) {
            logEntryRepository.deleteById(topEntry.getId());
        }
        return "redirect:/";
    }
}
