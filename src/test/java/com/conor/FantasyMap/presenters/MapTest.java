package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapTest {

    @Mock
    Location origin;

    @Mock
    Location notOrigin;

    @Mock
    LogEntry logOne;

    @Mock
    LogEntry logTwo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(origin.isOrigin()).thenReturn(true);
        when(origin.getX()).thenReturn((double) 0);
        when(origin.getY()).thenReturn((double) 0);
        when(notOrigin.getX()).thenReturn(300.0);
        when(notOrigin.getY()).thenReturn(400.0);
        when(notOrigin.calculateDistanceTo(origin)).thenReturn(500);
        when(logOne.getDeltaX()).thenReturn(25.0);
        when(logOne.getDeltaY()).thenReturn(25.0);
        when(logTwo.getDeltaX()).thenReturn(-50.0);
        when(logTwo.getDeltaY()).thenReturn(-50.0);
    }

    @Test
    void getMapShouldConvertLocationsIntoNamedPoints() {
        assertThat(new Map(List.of(origin), List.of()).getPoints().size()).isEqualTo(1);
    }

    @Test
    void getMapShouldCenterOnOriginLocation() {
        assertThat(new Map(List.of(origin), List.of()).getPoints().get(0).getXCoord()).isEqualTo(450);
        assertThat(new Map(List.of(origin), List.of()).getPoints().get(0).getYCoord()).isEqualTo(300);
    }

    @Test
    void getMapShouldNotThrowExceptionWhenNoLocationsExist() {
        new Map(emptyList(), emptyList());
    }

    @Test
    void getMapShouldScaleToIncludeAllNamedPoints() {
        assertThat(new Map(List.of(origin, notOrigin), List.of()).getScale()).isEqualTo(0.5);
    }

    @Test
    void getMapShouldScaleToIncludePartyPosition() {
        LogEntry logThree = mock(LogEntry.class);
        when(logThree.getDeltaX()).thenReturn(1000.0);
        when(logThree.getDeltaY()).thenReturn(1000.0);

        Map renderedMap = new Map(List.of(origin), List.of(logThree));
        assertThat(renderedMap.getPartyMapCoords().getX()).isLessThan(900 - 50);
        assertThat(renderedMap.getPartyMapCoords().getY()).isLessThan(600 - 50);
        assertThat(renderedMap.getPartyMapCoords().getX()).isGreaterThan(50);
        assertThat(renderedMap.getPartyMapCoords().getY()).isGreaterThan(50);
    }

    @Test
    void getMapShouldSetPartyCoordsToOriginWithNoLog() {
        assertThat(new Map(List.of(origin), List.of()).getPartyMapCoords().getX()).isEqualTo(450);
        assertThat(new Map(List.of(origin), List.of()).getPartyMapCoords().getY()).isEqualTo(300);
    }

    @Test
    void getMapShouldSetScaledPartyCoordsWithLogEntries() {
        Location locationCausingScaleToBeOne = new Location();
        locationCausingScaleToBeOne.setX(0);
        locationCausingScaleToBeOne.setY(250);
        assertThat(new Map(List.of(origin, locationCausingScaleToBeOne), List.of(logOne)).getPartyMapCoords().getX()).isEqualTo(475);
        assertThat(new Map(List.of(origin, locationCausingScaleToBeOne), List.of(logOne)).getPartyMapCoords().getY()).isEqualTo(275);
        assertThat(new Map(List.of(origin, locationCausingScaleToBeOne), List.of(logOne, logTwo)).getPartyMapCoords().getX()).isEqualTo(425);
        assertThat(new Map(List.of(origin, locationCausingScaleToBeOne), List.of(logOne, logTwo)).getPartyMapCoords().getY()).isEqualTo(325);
    }

    @Test
    void getMapShouldProvideCurrentDestination() {
        LogEntry logEntry = new LogEntry();
        Location bastion = new Location();
        bastion.setName("Bastion");
        logEntry.setDestination(bastion);

        assertThat(new Map(List.of(), List.of(logEntry)).getCurrentDestinationName()).isEqualTo("Bastion");
    }

    @Test
    void getMapShouldReturnNullWhenNoLogEntriesHaveDestinations() {
        assertThat(new Map(List.of(), List.of()).getCurrentDestinationName()).isEqualTo(null);
    }
}