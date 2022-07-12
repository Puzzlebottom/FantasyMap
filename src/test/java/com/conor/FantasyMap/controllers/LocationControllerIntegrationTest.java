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
        Elements markers = doc.getElementsByAttributeValue("data-test-id", "location");
        Elements destinationOptions = doc.getElementsByAttributeValue("data-test-id", "destination");

        assertThat(markers.size()).isEqualTo(2);
        assertThat(markers.get(0).attr("transform")).isEqualTo("translate(450.0,300.0)");
        assertThat(markers.get(1).attr("transform")).isEqualTo("translate(319.0,82.0)");

        assertThat(destinationOptions.size()).isEqualTo(2);
        assertThat(destinationOptions.get(0).text()).isEqualTo("Bastion");
        assertThat(destinationOptions.get(1).text()).isEqualTo("Cathedral");
    }

    @Test
    void shouldRenderElapsedTime() throws IOException {
        Document doc = testHelper.getDoc();
        assertThat(doc.body().text()).contains("Day 1, 12AM");
    }

    @Test
    void deleteLocationShouldDoSo() {
        testHelper.givenALocationExists("Bastion");
        testHelper.givenALocationExists("Cathedral");

        assertThat(testHelper.getAllLocationNames().size()).isEqualTo(2);

        testHelper.deleteLocation("Bastion");

        assertThat(testHelper.getAllLocationNames().size()).isEqualTo(1);
    }

    @Test
    void deleteLocationShouldDissociateLogEntriesFromDeletedLocation() throws IOException {
        testHelper.givenALocationExists("Bastion");
        testHelper.givenPartyHasMovedToDestination("Bastion", 8);

        Document doc = testHelper.getDoc();
        Elements entries = doc.getElementsByAttributeValue("data-test-id", "travel-log-entry");

        assertThat(entries.get(0).text()).isEqualTo("Party travelled 8 hours toward Bastion");

        testHelper.deleteLocation("Bastion");

        Document doc2 = testHelper.getDoc();
        Elements entries2 = doc2.getElementsByAttributeValue("data-test-id", "travel-log-entry");

        assertThat(entries2.get(0).text()).isEqualTo("Party travelled east for 8 hours");
    }
}
