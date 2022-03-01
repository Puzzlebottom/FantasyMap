package com.conor.FantasyMap.controllers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationControllerIntegrationTest extends IntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldUpdateInfoForALocation() {
        testHelper.givenALocationExists("Bastion");

        restTemplate.postForObject(testHelper.getBaseUri() + "/stored-locations/location/info?targetLocation=Bastion",
                "banana",
                String.class);

        List result = restTemplate.getForObject(testHelper.getBaseUri() + "/stored-locations/location/info?name=Bastion",
                List.class);

        assertThat(result).contains("banana");
    }

    @Test
    void shouldRenderLocationsOnMap() throws IOException {
        testHelper.givenALocationExists("Bastion");
        testHelper.givenALocationExists("Cathedral", -12, 20);

        Document doc = testHelper.getDoc();
        Elements circles = doc.select("[data-test-id=\"location\"]");

        assertThat(circles.size()).isEqualTo(2);
        assertThat(circles.get(0).attr("cx")).isEqualTo("450");
        assertThat(circles.get(0).attr("cy")).isEqualTo("300");
        assertThat(circles.get(1).attr("cx")).isEqualTo("319");
        assertThat(circles.get(1).attr("cy")).isEqualTo("82");
    }
}
