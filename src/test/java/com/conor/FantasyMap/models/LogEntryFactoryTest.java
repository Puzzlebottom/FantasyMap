package com.conor.FantasyMap.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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

}