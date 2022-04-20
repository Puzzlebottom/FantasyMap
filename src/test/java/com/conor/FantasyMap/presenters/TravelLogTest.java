package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.conor.FantasyMap.models.LogEntryFactory.createLogEntryByCourse;
import static com.conor.FantasyMap.models.LogEntryFactory.createLogEntryByDestination;
import static org.assertj.core.api.Assertions.assertThat;

class TravelLogTest {
    @Test
    void getFormattedTravelLogShouldReturnListOfFormattedMovements() {
        List<LogEntry> logEntries = List.of(createLogEntryByCourse("N", 24),
                createLogEntryByCourse("E", 24));
        TravelLog travelLog = new TravelLog(logEntries);

        assertThat(travelLog.getFormattedTravelLog()).containsExactly("Party travelled north for 24 hours", "Party travelled east for 24 hours");
    }

    @Test
    void getFormattedTravelLogShouldFormatForDestinationLogType() {
        Location destination = new Location();
        destination.setName("Margaritaville");
        destination.setX(25);
        destination.setY(25);
        List<LogEntry> logEntries = List.of(createLogEntryByDestination(new Point(0, 0), destination, 100));
        TravelLog travelLog = new TravelLog(logEntries);

        assertThat(travelLog.getFormattedTravelLog()).containsExactly("Party travelled 100 hours toward Margaritaville");
    }

    @Test
    void getElapsedTimeShouldReturnAfternoonTime() {
        List<LogEntry> logEntries = List.of(createLogEntryByCourse("N", 14),
                createLogEntryByCourse("E", 24));
        TravelLog travelLog = new TravelLog(logEntries);

        assertThat(travelLog.getElapsedTime()).isEqualTo("Day 2, 2PM");
    }

    @Test
    void getElapsedTimeShouldReturnMorningTime() {
        List<LogEntry> logEntries = List.of(createLogEntryByCourse("N", 36),
                createLogEntryByCourse("E", 37));
        TravelLog travelLog = new TravelLog(logEntries);

        assertThat(travelLog.getElapsedTime()).isEqualTo("Day 4, 1AM");
    }

    @Test
    void getElapsedTimeShouldHandleMidnightCases() {
        List<LogEntry> logEntries = List.of();
        TravelLog travelLog = new TravelLog(logEntries);

        assertThat(travelLog.getElapsedTime()).isEqualTo("Day 1, 12AM");
    }
}