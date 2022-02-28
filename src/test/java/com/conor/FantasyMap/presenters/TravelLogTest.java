package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.models.LogEntryFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.conor.FantasyMap.models.LogEntryFactory.createLogEntryByCourse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TravelLogTest {
    @Test
    void getMovementsShouldReturnListOfFormattedMovements() {
        List<LogEntry> logsEntries = List.of(createLogEntryByCourse("N", 24),
                createLogEntryByCourse("E", 24));
        TravelLog travelLog = new TravelLog(logsEntries);

        assertThat(travelLog.getMovements()).containsExactly("Party travelled north for 24 hours", "Party travelled east for 24 hours");
    }
}