package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import com.conor.FantasyMap.repositories.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldUpdateInfoForALocation() {
        String baseUri = "http://localhost:" + port;
        Location location = new Location();
        location.setName("Bastion");

        restTemplate.postForObject(baseUri + "/locations", location, Object.class);

        restTemplate.postForObject(baseUri + "/locations/info?targetLocation=Bastion",
                "banana",
                String.class);

        List result = restTemplate.getForObject(baseUri + "/locations/info?name=Bastion",
                List.class);


        assertThat(result).contains("banana");
    }
}
