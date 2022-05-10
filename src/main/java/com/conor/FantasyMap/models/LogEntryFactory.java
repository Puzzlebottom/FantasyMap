package com.conor.FantasyMap.models;

public class LogEntryFactory {
    private static final int SPEED_PER_HOUR = 3;

    public static LogEntry createLogEntryByCourse(String direction, int deltaHours) {
        double travelDistance = deltaHours * SPEED_PER_HOUR;
        CardinalDirection cardinal = CardinalDirection.valueOf(direction);
        double directionInDegrees = cardinal.toAngle();
        double deltaX = travelDistance * Math.sin(Math.toRadians(directionInDegrees));
        double deltaY = travelDistance * Math.cos(Math.toRadians(directionInDegrees));
        LogEntry logEntry = new LogEntry();
        logEntry.setDeltaX(deltaX);
        logEntry.setDeltaY(deltaY);
        logEntry.setDeltaHours(deltaHours);
        logEntry.setType(LogEntryType.COURSE);
        return logEntry;
    }

    public static LogEntry createLogEntryByDestination(IPoint partyPosition, Location destination, int deltaHours) {
        int currentX = (int) partyPosition.getX();
        int currentY = (int) partyPosition.getY();
        int totalDistance = partyPosition.calculateDistanceTo(destination);
        double travelDistance = Math.min((deltaHours * SPEED_PER_HOUR), totalDistance);
        double x = partyPosition.getX() + travelDistance/totalDistance * (destination.getX() - partyPosition.getX());
        double y = partyPosition.getY() + travelDistance/totalDistance * (destination.getY() - partyPosition.getY());
        double deltaX = x - currentX;
        double deltaY = y - currentY;
        LogEntry logEntry = new LogEntry();
        logEntry.setDeltaX(deltaX);
        logEntry.setDeltaY(deltaY);
        logEntry.setDeltaHours(deltaHours);
        logEntry.setType(LogEntryType.DESTINATION);
        logEntry.setDestination(destination);
        return logEntry;
    }
}
