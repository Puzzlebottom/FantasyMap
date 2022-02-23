package com.conor.FantasyMap.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

import static java.lang.Math.*;

@Entity
@Getter
@Setter
public class LogEntry {
    @GeneratedValue
    @Id
    private Long id;
    private double deltaX;
    private double deltaY;
    private int deltaHours;

    public static Point sumPositionalDelta(List<LogEntry> log) {
        int x = (int) log.stream().mapToDouble(LogEntry::getDeltaX).sum();
        int y = (int) log.stream().mapToDouble(LogEntry::getDeltaY).sum();
        return new Point(x, y);
    }

    public String getDirectionName() {
        double theta = toDegrees(atan2(-this.deltaY, this.deltaX));
        CardinalDirection cardinalDirection = CardinalDirection.ofAngle((int) round(theta) + 90);
        return cardinalDirection.getName();
    }
}
