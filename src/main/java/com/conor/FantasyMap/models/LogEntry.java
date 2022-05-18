package com.conor.FantasyMap.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.*;

@Entity
@Getter
@Setter
public class LogEntry {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private double deltaX;
    private double deltaY;
    private int deltaHours;
    private LogEntryType type;
    @ManyToOne
    @JoinColumn(name="location_id")
    private Location destination;

    public static Point sumPositionalDelta(List<LogEntry> log) {
        int x = (int) log.stream().mapToDouble(LogEntry::getDeltaX).sum();
        int y = (int) log.stream().mapToDouble(LogEntry::getDeltaY).sum();
        return new Point(x, y);
    }

    public static Optional<Location> getCurrentDestination(List<LogEntry> log) {
        List<Location> destinations = log.stream()
                .filter(logEntry -> logEntry.getDestination() != null)
                .map(LogEntry::getDestination).toList();
        if(destinations.size() < 1) {
            return Optional.empty();
        }
        return Optional.of(destinations.get(destinations.size() - 1));
    }

    public String getDirectionName() {
        double theta = toDegrees(atan2(-this.deltaY, this.deltaX));
        CardinalDirection cardinalDirection = CardinalDirection.ofAngle((int) round(theta) + 90);
        return cardinalDirection.getName();
    }

}
