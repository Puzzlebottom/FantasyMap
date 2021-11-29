package com.conor.FantasyMap.services;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.models.Map;
import com.conor.FantasyMap.models.NamedPoint;
import com.conor.FantasyMap.repositories.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MapServiceTest {

    @InjectMocks
    MapService mapService;

    @Mock
    LocationRepository locationRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getScaledMapShouldCenterASingleLocation() {
        Location location = new Location();
        location.setName("Bastion");
        location.setXCoord(10);
        location.setYCoord(20);

        Map scaledMap = mapService.getScaledMap(List.of(location));
        scaledMap.setHeight(600);
        scaledMap.setWidth(900);

        assertThat(scaledMap.getPoints().get(0).getXCoord()).isEqualTo(450);
        assertThat(scaledMap.getPoints().get(0).getYCoord()).isEqualTo(300);
    }
}