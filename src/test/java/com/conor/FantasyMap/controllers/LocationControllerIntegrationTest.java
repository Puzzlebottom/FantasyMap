package com.conor.FantasyMap.controllers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class LocationControllerIntegrationTest extends IntegrationTest {

    @Test
    void shouldUpdateInfoForALocation() {
        testHelper.givenALocationExists("Bastion");

        testHelper.exchange("/locations/Bastion/info", POST,
                "banana");
        ResponseEntity<Map<String, Object>> result = testHelper.exchange("/locations/Bastion", GET,
                null);
        List<String> testInfo = (List<String>) result.getBody().get("info");

        assertThat(testInfo).contains("banana");
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

    @Test
    void shouldRenderElapsedTime() throws IOException {
        Document doc = testHelper.getDoc();
        assertThat(doc.body().text()).contains("Day 1, 12AM");
    }
    //TODO Make destination selectors auto-poopulate from db
}
