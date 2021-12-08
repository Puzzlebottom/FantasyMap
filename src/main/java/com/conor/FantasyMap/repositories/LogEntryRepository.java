package com.conor.FantasyMap.repositories;

import com.conor.FantasyMap.models.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    List<LogEntry> findAll();
}
