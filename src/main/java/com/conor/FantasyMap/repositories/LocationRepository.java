package com.conor.FantasyMap.repositories;

import com.conor.FantasyMap.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAll();
    Location findLocationByName(String locationName);
    Location findLocationByIsDestinationIsTrue();
    void deleteByName(String locationName);
}
