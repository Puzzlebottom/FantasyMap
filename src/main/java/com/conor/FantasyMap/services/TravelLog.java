package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.IPoint;
import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.Point;

import java.util.List;

public interface TravelLog {
    int speedPerHour = 3;

    static Point sumPositionalDelta(List<LogEntry> log) {
        int x = (int) log.stream().mapToDouble(LogEntry::getDeltaX).sum();
        int y = (int) log.stream().mapToDouble(LogEntry::getDeltaY).sum();
        return new Point(x, y);
    }

    static LogEntry createLogEntryByCourse(int directionInDegrees, int deltaHours) {
        double travelDistance = deltaHours * speedPerHour;
        double deltaX = travelDistance * Math.cos(directionInDegrees);
        double deltaY = travelDistance * Math.sin(directionInDegrees);
        LogEntry logEntry = new LogEntry();
        logEntry.setDeltaX(deltaX);
        logEntry.setDeltaY(deltaY);
        logEntry.setDeltaHours(deltaHours);
        return logEntry;
    }

    static LogEntry createLogEntryByDestination(IPoint partyPosition, IPoint destination, int deltaHours) {
        double travelDistance = deltaHours * speedPerHour;
        int currentX = (int) partyPosition.getX();
        int currentY = (int) partyPosition.getY();
        int totalDistance = partyPosition.calculateDistanceTo(destination);
        double x = partyPosition.getX() + travelDistance/totalDistance * (destination.getX() - partyPosition.getX());
        double y = partyPosition.getY() + travelDistance/totalDistance * (destination.getY() - partyPosition.getY());
        double deltaX = x - currentX;
        double deltaY = y - currentY;
        LogEntry logEntry = new LogEntry();
        logEntry.setDeltaX(deltaX);
        logEntry.setDeltaY(deltaY);
        logEntry.setDeltaHours(deltaHours);
        return logEntry;
    }
}
