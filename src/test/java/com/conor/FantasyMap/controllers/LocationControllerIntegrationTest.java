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
    private TestRestTemplate restTemplate;
    private String baseUri;

    @BeforeEach
    void setup() {
        baseUri = "http://localhost:" + port;
    }

    @Test
    void shouldUpdateInfoForALocation() {
        givenALocationExists("Bastion");

        restTemplate.postForObject(baseUri + "/stored-locations/location/info?targetLocation=Bastion",
                "banana",
                String.class);

        List result = restTemplate.getForObject(baseUri + "/stored-locations/location/info?name=Bastion",
                List.class);

        assertThat(result).contains("banana");
    }

    @Test
    void shouldRenderLocationsOnMap() throws IOException {
        givenALocationExists("Bastion");
        givenALocationExists("Cathedral", -12, 20);

        Document doc = Jsoup.connect(baseUri).get();
        Elements circles = doc.select("circle");
        circles.forEach(System.out::println);

        assertThat(circles.size()).isEqualTo(2);
        assertThat(circles.get(0).attr("cx")).isEqualTo("0");
        assertThat(circles.get(0).attr("cy")).isEqualTo("0");
        assertThat(circles.get(1).attr("cx")).isEqualTo("-12");
        assertThat(circles.get(1).attr("cy")).isEqualTo("-20");
    }

    private void givenALocationExists(String name, int xCoord, int yCoord) {
        Location location = new Location();
        location.setName(name);
        location.setXCoord(xCoord);
        location.setYCoord(yCoord);

        restTemplate.postForObject(baseUri + "/stored-locations", location, Object.class);
    }

    private void givenALocationExists(String name) {
        givenALocationExists(name, 0, 0);
    }
}
