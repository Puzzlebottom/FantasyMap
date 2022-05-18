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

        assertThat(first.getCurrentDestination(logList).get().getName()).isEqualTo("destination");
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

        assertThat(second.getCurrentDestination(logList).get().getName()).isEqualTo("destination");
    }

    @Test
    void getCurrentDestinationShouldIgnoreLogEntriesWithNoDestinations() {
        Location bastion = new Location();
        bastion.setName("Bastion");
        Location cathedral = new Location();
        cathedral.setName("Cathedral");
        LogEntry toCathedral = new LogEntry();
        toCathedral.setDestination(cathedral);
        LogEntry withoutDestination = new LogEntry();
        LogEntry toBastion = new LogEntry();
        toBastion.setDestination(bastion);

        List<LogEntry> logList = List.of(toBastion, withoutDestination);

        assertThat(LogEntry.getCurrentDestination(logList).get().getName()).isEqualTo("Bastion");
    }

    @Test
    void getCurrentDestinationShouldReturnEmptyOptionalIfNoLogEntriesHaveDestinations() {
        assertThat(LogEntry.getCurrentDestination(List.of()).isPresent()).isFalse();
    }
}