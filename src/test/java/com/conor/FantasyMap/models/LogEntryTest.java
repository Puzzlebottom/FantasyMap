package com.conor.FantasyMap.models;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LogEntryTest {

    @Test
    void getCurrentDestinationShouldReturnALocation() {
        Location destination = new Location();
        destination.setName("destination");
        LogEntry first = new LogEntry();
        first.setDestination(destination);
        List<LogEntry> logList = List.of(first);

        assertThat(first.getCurrentDestination(logList).getName()).isEqualTo("destination");
    }

    @Test
    void getCurrentDestinationShouldReturnLastLocationLogged() {
        Location destination = new Location();
        destination.setName("destination");
        Location notDestination = new Location();
        notDestination.setName("notDestination");
        LogEntry first = new LogEntry();
        first.setDestination(notDestination);
        LogEntry second = new LogEntry();
        second.setDestination(destination);
        List<LogEntry> logList = List.of(first, second);

        assertThat(second.getCurrentDestination(logList).getName()).isEqualTo("destination");
    }

    @Test
    @Ignore
    void getCurrentDestinationShouldHandleEntriesWithNoDestination() {
        Location destination = new Location();
        destination.setName("destination");
        Location notDestination = new Location();
        notDestination.setName("notDestination");
        LogEntry first = new LogEntry();
        first.setDestination(notDestination);
        LogEntry emptyCase = new LogEntry();
        LogEntry second = new LogEntry();
        second.setDestination(destination);
        List<LogEntry> logList = List.of(emptyCase, first, emptyCase, second, emptyCase);

        assertThat(second.getCurrentDestination(logList).getName()).isEqualTo("destination");
    }
}