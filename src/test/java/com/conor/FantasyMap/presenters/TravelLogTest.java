package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.LogEntryType;
import com.conor.FantasyMap.models.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.conor.FantasyMap.models.LogEntryFactory.createLogEntryByCourse;
import static com.conor.FantasyMap.models.LogEntryFactory.createLogEntryByDestination;
import static com.conor.FantasyMap.models.LogEntryType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelLogTest {
    @Test
    void getFormattedTravelLogShouldReturnListOfFormattedMovements() {
        LogEntry mockEntry1 = mock(LogEntry.class);
        when(mockEntry1.getId()).thenReturn(1L);
        when(mockEntry1.getDirectionName()).thenReturn("north");
        when(mockEntry1.getDeltaHours()).thenReturn(24);
        when(mockEntry1.getType()).thenReturn(COURSE);
        LogEntry mockEntry2 = mock(LogEntry.class);
        when(mockEntry2.getId()).thenReturn(2L);
        when(mockEntry2.getDirectionName()).thenReturn("east");
        when(mockEntry2.getDeltaHours()).thenReturn(24);
        when(mockEntry2.getType()).thenReturn(COURSE);
        List<LogEntry> logEntries = List.of(mockEntry1, mockEntry2);

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