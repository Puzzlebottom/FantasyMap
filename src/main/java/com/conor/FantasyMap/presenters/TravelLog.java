package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.LogEntryFactory;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.conor.FantasyMap.models.LogEntryType.*;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class TravelLog {

    private final List<LogEntry> logEntries;

    public List<String> getFormattedTravelLog() {
        return logEntries.stream()
                .sorted(Comparator.comparing(LogEntry::getId))
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
        String restFormat = "Party remained in place for %s hours";
        String teleportFormat = "Party teleported to %s";
        String fastTravelFormat = "Party travels for %s and arrives at %s";

        if (logEntry.getType() == COURSE) {
            return courseFormat.formatted(logEntry.getDirectionName(), logEntry.getDeltaHours());
        } else if (logEntry.getType() == DESTINATION) {
            return destinationFormat.formatted(logEntry.getDeltaHours(), logEntry.getDestination().getName());
        } else if (logEntry.getType() == REST) {
            return restFormat.formatted(logEntry.getDeltaHours());
        } else if (logEntry.getType() == TELEPORT) {
            return teleportFormat.formatted(logEntry.getDestination().getName());
        } else if (logEntry.getType() == FAST_TRAVEL) {
            return fastTravelFormat.formatted(formatFastTravelTime(logEntry.getDeltaHours()), logEntry.getDestination().getName());
        }
        throw new NotYetImplementedException();
    }

    private String formatFastTravelTime(int deltaHours) {
        if (deltaHours == 1) {
            return "one hour";
        } else if(deltaHours <= 24) {
            return deltaHours + " hours";
        } else if(deltaHours % 24 == 0) {
            return (deltaHours / 24) + " days";
        } else {
            int days = deltaHours / 24;
            int hours = deltaHours % 24;
            return days + " days and " + hours + " hours";
        }
    }
}
