package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;
import java.util.Optional;

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

    public String getElapsedTime() {
        String template = "Day %s, %s%s";
        int sumHours = logEntries.stream()
                .map(LogEntry::getDeltaHours)
                .reduce(0, Integer::sum);

        String day = String.valueOf((sumHours / 24) + 1);
        int hour = sumHours % 24;
        String formattedHour = String.valueOf(hour % 12);
        String amPm = hour >= 12 ? "PM" : "AM";
        if (hour == 0) {
            return template.formatted(day, 12, "AM");
        }
        return template.formatted(day, formattedHour, amPm);
    }

    private String getFormattedEntry(LogEntry logEntry) {
        String courseFormat = "Party travelled %s for %s hours";
        String destinationFormat = "Party travelled %s hours toward %s";

        if (logEntry.getType() == COURSE) {
            return courseFormat.formatted(logEntry.getDirectionName(), logEntry.getDeltaHours());
        } else if (logEntry.getType() == DESTINATION) {
            return destinationFormat.formatted(logEntry.getDeltaHours(), logEntry.getDestination().getName());
        }
        throw new NotYetImplementedException();
    }
}
