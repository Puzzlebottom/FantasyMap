package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.LogEntry;
import com.conor.FantasyMap.presenters.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapTest {

    @InjectMocks
    Map map;

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
        assertThat(map.getMap(List.of(origin), List.of()).getPoints().size()).isEqualTo(1);
    }

    @Test
    void getMapShouldCenterOnOriginLocation() {
        assertThat(map.getMap(List.of(origin), List.of()).getPoints().get(0).getXCoord()).isEqualTo(450);
        assertThat(map.getMap(List.of(origin), List.of()).getPoints().get(0).getYCoord()).isEqualTo(300);
    }

    @Test
    void getMapShouldNotThrowExceptionWhenNoLocationsExist() {
        map.getMap(emptyList(), emptyList());
    }

    @Test
    void getMapShouldScaleToIncludeAllNamedPoints() {
        assertThat(map.getMap(List.of(origin, notOrigin), List.of()).getScale()).isEqualTo(0.5);
    }

    @Test
    void getMapShouldScaleToIncludePartyPosition() {
        LogEntry logThree = mock(LogEntry.class);
        when(logThree.getDeltaX()).thenReturn(1000.0);
        when(logThree.getDeltaY()).thenReturn(1000.0);

        Map renderedMap = map.getMap(List.of(origin), List.of(logThree));
        assertThat(renderedMap.getPartyMapCoords().getX()).isLessThan(900 - 50);
        assertThat(renderedMap.getPartyMapCoords().getY()).isLessThan(600 - 50);
        assertThat(renderedMap.getPartyMapCoords().getX()).isGreaterThan(50);
        assertThat(renderedMap.getPartyMapCoords().getY()).isGreaterThan(50);
    }

    @Test
    void getMapShouldSetPartyCoordsToOriginWithNoLog() {
        assertThat(map.getMap(List.of(origin), List.of()).getPartyMapCoords().getX()).isEqualTo(450);
        assertThat(map.getMap(List.of(origin), List.of()).getPartyMapCoords().getY()).isEqualTo(300);
    }

    @Test
    void getMapShouldSetScaledPartyCoordsWithLogEntries() {
        Location locationCausingScaleToBeOne = new Location();
        locationCausingScaleToBeOne.setX(0);
        locationCausingScaleToBeOne.setY(250);
        assertThat(map.getMap(List.of(origin, locationCausingScaleToBeOne), List.of(logOne)).getPartyMapCoords().getX()).isEqualTo(475);
        assertThat(map.getMap(List.of(origin, locationCausingScaleToBeOne), List.of(logOne)).getPartyMapCoords().getY()).isEqualTo(275);
        assertThat(map.getMap(List.of(origin, locationCausingScaleToBeOne), List.of(logOne, logTwo)).getPartyMapCoords().getX()).isEqualTo(425);
        assertThat(map.getMap(List.of(origin, locationCausingScaleToBeOne), List.of(logOne, logTwo)).getPartyMapCoords().getY()).isEqualTo(325);
    }
}