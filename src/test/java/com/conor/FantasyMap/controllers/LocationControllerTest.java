package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.CardinalDirection;
import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.repositories.LocationRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @Captor
    ArgumentCaptor<Location> locationCaptor;

    @InjectMocks
    LocationController locationController;

    @Mock
    LocationRepository locationRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocationsShouldReturnLocations() {
        Location mockLocation = mock(Location.class);
        when(locationRepository.findAll()).thenReturn(List.of(mockLocation));
        List<Location> allLocations = locationController.getAllLocations();
        assertThat(allLocations).contains(mockLocation);
    }

    @Test
    void addLocationInfoShouldDoSo() {
        Location location = new Location();
        location.setInfo(Collections.emptyList());
        when(locationRepository.findLocationByName("Bastion")).thenReturn(location);

        locationController.addLocationInfo("Bastion", "Bastion is a city");

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getInfo()).contains("Bastion is a city");
    }

    @Test
    void addRelativeLocationShouldSaveALocationWithCoordinates() {
        Location cathedral = new Location();
        cathedral.setName("Cathedral");
        Location bastion = new Location();
        bastion.setX(0);
        bastion.setY(0);
        when(locationRepository.findLocationByName("Bastion")).thenReturn(bastion);

        locationController.addRelativeLocation("Bastion", "NW", 100, cathedral);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getName()).isEqualTo("Cathedral");
        assertThat(locationCaptor.getValue().getX()).isEqualTo(-71.0);
        assertThat(locationCaptor.getValue().getY()).isEqualTo(71.0);
    }
}