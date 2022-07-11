package com.conor.FantasyMap.repositories;

import com.conor.FantasyMap.models.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    List<LogEntry> findAll();

    LogEntry findFirstByOrderByIdDesc();

    void deleteById(Long id);

    @Modifying
    @Query("UPDATE LogEntry  SET type = 0, destination = null where destination.id = :locationId")
    void removeDestinationRef(@Param("locationId") Long locationId);

}
