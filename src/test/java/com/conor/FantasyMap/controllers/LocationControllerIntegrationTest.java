package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocationControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldUpdateInfoForALocation() {
        integrationTestHelper.givenALocationExists(port, "Bastion");

        restTemplate.postForObject(integrationTestHelper.getBaseUri(port) + "/stored-locations/location/info?targetLocation=Bastion",
                "banana",
                String.class);

        List result = restTemplate.getForObject(integrationTestHelper.getBaseUri(port) + "/stored-locations/location/info?name=Bastion",
                List.class);

        assertThat(result).contains("banana");
    }

    @Test
    void shouldRenderLocationsOnMap() throws IOException {
        integrationTestHelper.givenALocationExists(port, "Bastion");
        integrationTestHelper.givenALocationExists(port, "Cathedral", -12, 20);

        Document doc = integrationTestHelper.getDoc(port);
        Elements circles = doc.select("[data-test-id=\"location\"]");

        assertThat(circles.size()).isEqualTo(2);
        assertThat(circles.get(0).attr("cx")).isEqualTo("450");
        assertThat(circles.get(0).attr("cy")).isEqualTo("300");
        assertThat(circles.get(1).attr("cx")).isEqualTo("319");
        assertThat(circles.get(1).attr("cy")).isEqualTo("82");
    }
}
