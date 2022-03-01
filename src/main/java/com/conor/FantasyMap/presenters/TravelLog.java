package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.LogEntry;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

import static com.conor.FantasyMap.models.LogEntryType.COURSE;
import static com.conor.FantasyMap.models.LogEntryType.DESTINATION;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class TravelLog {

    private final List<LogEntry> logEntries;

    public List<String> getFormattedTravelLog() {
        return logEntries.stream()
                .map(this::getFormattedEntry)
                .collect(toList());
    }

    private String getFormattedEntry(LogEntry logEntry) {
        String courseFormat = "Party travelled %s for %s hours";
        String destinationFormat = "Party travelled %s hours toward %s";

        if (logEntry.getType() == COURSE) {
            return courseFormat.formatted(logEntry.getDirectionName(), logEntry.getDeltaHours());
        } else if (logEntry.getType() == DESTINATION) {
            return destinationFormat.formatted(logEntry.getDeltaHours(), logEntry.getLocation().getName());
        }
        throw new NotYetImplementedException();
    }
}
