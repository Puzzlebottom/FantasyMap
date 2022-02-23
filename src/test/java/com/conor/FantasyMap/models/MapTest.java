package com.conor.FantasyMap.models;

import com.conor.FantasyMap.presenters.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        when(origin.getXCoord()).thenReturn(0);
        when(origin.getYCoord()).thenReturn(0);
        when(notOrigin.getXCoord()).thenReturn(300);
        when(notOrigin.getYCoord()).thenReturn(400);
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
    void getMapShouldScaleToIncludeAllNamedPoints() {
        assertThat(map.getMap(List.of(origin, notOrigin), List.of()).getScale()).isEqualTo(0.5);
    }

    @Test
    void getMapShouldSetPartyCoordsToOriginWithNoLog() {
        assertThat(map.getMap(List.of(origin), List.of()).getPartyMapCoords().getX()).isEqualTo(450);
        assertThat(map.getMap(List.of(origin), List.of()).getPartyMapCoords().getY()).isEqualTo(300);
    }

    @Test
    void getMapShouldSetPartyCoordsCorrectlyWithLog() {
        assertThat(map.getMap(List.of(origin), List.of(logOne)).getPartyMapCoords().getX()).isEqualTo(475);
        assertThat(map.getMap(List.of(origin), List.of(logOne)).getPartyMapCoords().getY()).isEqualTo(275);
        assertThat(map.getMap(List.of(origin), List.of(logOne, logTwo)).getPartyMapCoords().getX()).isEqualTo(425);
        assertThat(map.getMap(List.of(origin), List.of(logOne, logTwo)).getPartyMapCoords().getY()).isEqualTo(325);

    }
}