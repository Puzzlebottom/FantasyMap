package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.repositories.LocationRepository;
import com.conor.FantasyMap.repositories.LogEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @Captor
    ArgumentCaptor<Location> locationCaptor;

    @InjectMocks
    LocationController locationController;

    @Mock
    LocationRepository locationRepository;

    @Mock
    LogEntryRepository logEntryRepository;

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
        when(locationRepository.findLocationByName("Bastion")).thenReturn(location);
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setName("Bastion");
        request.setInfo("Bastion is a city");

        locationController.addLocationInfo(request);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getInfo()).isEqualTo("Bastion is a city");
    }

    @Test
    void addRelativeLocationShouldSaveALocationWithCoordinates() {
        Location cathedral = new Location();
        cathedral.setName("Cathedral");
        Location bastion = new Location();
        bastion.setX(0);
        bastion.setY(0);
        NewLocationRequest request = new NewLocationRequest();
        request.setOrigin("Bastion");
        request.setDirection("NW");
        request.setDistance(100);
        request.setNewLocationName("Cathedral");
        when(locationRepository.findLocationByName("Bastion")).thenReturn(bastion);

        locationController.addRelativeLocation(request);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getName()).isEqualTo("Cathedral");
        assertThat(locationCaptor.getValue().getX()).isEqualTo(-71.0);
        assertThat(locationCaptor.getValue().getY()).isEqualTo(71.0);
    }

    @Test
    void addLocationAtPartyPositionShouldSaveALocationWithCoordinates() {
        NewLocationRequest request = new NewLocationRequest();
        request.setNewLocationName("Bastion");
        when(logEntryRepository.findAll()).thenReturn(List.of());

        locationController.addLocationAtPartyPosition(request);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getName()).isEqualTo("Bastion");
        assertThat(locationCaptor.getValue().getX()).isEqualTo(0.0);
        assertThat(locationCaptor.getValue().getY()).isEqualTo(0.0);
    }

    @Test
    void addLocationAtPartyPositionShouldCalculateCoordinatesFromLogEntries() {
        NewLocationRequest request = new NewLocationRequest();
        request.setNewLocationName("Bastion");
        LogEntry entry = new LogEntry();
        entry.setDeltaX(49);
        entry.setDeltaY(-81);
        when(logEntryRepository.findAll()).thenReturn(List.of(entry));

        locationController.addLocationAtPartyPosition(request);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().getName()).isEqualTo("Bastion");
        assertThat(locationCaptor.getValue().getX()).isEqualTo(49.0);
        assertThat(locationCaptor.getValue().getY()).isEqualTo(-81.0);
    }

    @Test
    void addLocationAtPartyPositionShouldCreateOriginLocationIfNoLocationsExist() {
        NewLocationRequest request = new NewLocationRequest();
        request.setNewLocationName("Bastion");

        locationController.addLocationAtPartyPosition(request);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().isOrigin()).isTrue();
    }

    @Test
    void addLocationAtPartyPositionShouldCreateNonOriginLocationIfOtherLocationsExist() {
        NewLocationRequest request = new NewLocationRequest();
        request.setNewLocationName("Bastion");
        Location existingLocation = mock(Location.class);
        when(locationRepository.findAll()).thenReturn(List.of(existingLocation));

        locationController.addLocationAtPartyPosition(request);

        verify(locationRepository).save(locationCaptor.capture());
        assertThat(locationCaptor.getValue().isOrigin()).isFalse();
    }

    @Test
    void deleteLocationShouldNotDeleteOrigin() throws Exception {
        Location origin = new Location();
        origin.setName("Bastion");
        origin.setId(1L);
        DeleteLocationRequest request = new DeleteLocationRequest();
        request.setLocationName(origin.getName());
        when(locationRepository.findLocationByName(origin.getName())).thenReturn(origin);

        locationController.deleteLocation(request);

        verify(logEntryRepository, times(0)).removeDestinationRef(origin.getId());
        verify(locationRepository, times(0)).deleteByName(origin.getName());
    }
}