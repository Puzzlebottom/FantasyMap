package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.LogEntry;
import lombok.AllArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class TravelLog {

    private final List<LogEntry> logEntries;

    public List<String> getMovements() {
        return logEntries.stream()
                .map(logEntry -> "Party travelled %s for %s hours".formatted(logEntry.getDirectionName(), logEntry.getDeltaHours()))
                .collect(toList());
    }
}
