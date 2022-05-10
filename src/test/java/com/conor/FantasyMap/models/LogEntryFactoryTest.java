package com.conor.FantasyMap.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class LogEntryFactoryTest {

    @Test
    void createLogEntryByCourseShouldReturnForNorthEightHours() {
        LogEntry entry = LogEntryFactory.createLogEntryByCourse("N", 8);

        assertThat(entry.getDeltaHours()).isEqualTo(8);
        assertThat(entry.getDeltaX()).isEqualTo(0);
        assertThat(entry.getDeltaY()).isEqualTo(8 * 3);
    }

    @Test
    void createLogEntryByCourseShouldReturnForSouthEastTenHours() {
        LogEntry entry = LogEntryFactory.createLogEntryByCourse("SE", 10);

        assertThat(entry.getDeltaHours()).isEqualTo(10);
        assertThat(entry.getDeltaX()).isBetween(21.212, 21.214);
        assertThat(entry.getDeltaY()).isBetween(-21.214, -21.212);
    }

    @Test
    void getDirectionNameShouldReturnSouth() {
        LogEntry entry = LogEntryFactory.createLogEntryByCourse("S", 10);
        assertThat(entry.getDirectionName()).isEqualTo("south");
    }

    @Test
    void getDirectionNameShouldReturnEast() {
        LogEntry entry = LogEntryFactory.createLogEntryByCourse("E", 10);
        assertThat(entry.getDirectionName()).isEqualTo("east");
    }

    @Test
    void getDirectionNameShouldReturnWest() {
        LogEntry entry = LogEntryFactory.createLogEntryByCourse("W", 10);
        assertThat(entry.getDirectionName()).isEqualTo("west");
    }

    @Test
    void getDirectionNameShouldReturnNorth() {
        LogEntry entry = LogEntryFactory.createLogEntryByCourse("N", 10);
        assertThat(entry.getDirectionName()).isEqualTo("north");
    }

    @Test
    void getDirectionNameShouldReturnSoutheastForIntermediaryAngle() {
        LogEntry entry = new LogEntry();
        entry.setDeltaX(13);
        entry.setDeltaY(-11);
        assertThat(entry.getDirectionName()).isEqualTo("southeast");
    }

    @Test
    void createLogEntryByDestinationShouldCreateALogEntry() {
        Location location = new Location();
        location.setName("Destination");
        location.setX(100);
        location.setY(100);
        location.setId(5L);
        LogEntry entry = LogEntryFactory.createLogEntryByDestination(new Point(0, 0), location, 100);

        assertThat(entry.getDeltaHours()).isEqualTo(100);
        assertThat(entry.getDeltaX()).isEqualTo(100);
        assertThat(entry.getDeltaY()).isEqualTo(100);
        assertThat(entry.getDestination().getId()).isEqualTo(location.getId());
    }

}