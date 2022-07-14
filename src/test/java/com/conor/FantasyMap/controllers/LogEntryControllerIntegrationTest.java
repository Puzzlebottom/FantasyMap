package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.LogEntry;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

class LogEntryControllerIntegrationTest extends IntegrationTest {
    @Test
    void undoMoveShouldDeleteLastLogEntry() throws IOException {
        testHelper.givenPartyHasMoved("N", 8);
        testHelper.givenPartyHasMoved("E", 12);
        testHelper.givenPartyHasMoved("SW", 9);

        testHelper.exchange("/log-entries/delete-top-entry", POST, null);

        Document doc = testHelper.getDoc();
        Elements entries = doc.getElementsByAttributeValue("data-test-id", "travel-log-entry");

        assertThat(entries.size()).isEqualTo(2);
        assertThat(entries.get(0).text()).isEqualTo("Party travelled north for 8 hours");
        assertThat(entries.get(1).text()).isEqualTo("Party travelled east for 12 hours");
    }

    @Test
    void restInPlaceShouldAdvanceElapsedTime() throws IOException {
        testHelper.givenPartyHasRested(37);

        Document doc = testHelper.getDoc();
        assertThat(doc.body().text()).contains("Day 2, 1PM");
    }

    @Test
    void restInPlaceShouldNotMoveTheParty() throws IOException {
        Document doc = testHelper.getDoc();
        Element party = doc.getElementsByAttributeValue("data-test-id", "party-marker").get(0);
        int initialX = Integer.parseInt(party.attr("x"));
        int initialY = Integer.parseInt(party.attr("y"));

        testHelper.givenPartyHasRested(37);

        Document doc2 = testHelper.getDoc();
        Element party2 = doc2.getElementsByAttributeValue("data-test-id", "party-marker").get(0);
        int newX = Integer.parseInt(party2.attr("x"));
        int newY = Integer.parseInt(party2.attr("y"));

        assertThat(initialX).isEqualTo(newX);
        assertThat(initialY).isEqualTo(newY);
    }

    @Test
    void teleportToDestinationShouldMoveThePartyToDestination() throws IOException {
        testHelper.givenALocationExists("Cathedral", 137, -137);
        Document doc = testHelper.getDoc();
        Element party = doc.getElementsByAttributeValue("data-test-id", "party-marker").get(0);
        int initialX = Integer.parseInt(party.attr("x"));
        int initialY = Integer.parseInt(party.attr("y"));

        testHelper.givenPartyHasTeleported("Cathedral");

        Document doc2 = testHelper.getDoc();
        Element party2 = doc2.getElementsByAttributeValue("data-test-id", "party-marker").get(0);
        int newX = Integer.parseInt(party2.attr("x"));
        int newY = Integer.parseInt(party2.attr("y"));

        assertThat(initialX).isNotEqualTo(newX);
        assertThat(initialY).isNotEqualTo(newY);
        assertThat(newX).isEqualTo(611);
        assertThat(newY).isEqualTo(446);
    }

    @Test
    void teleportToDestinationShouldNotAdvanceElapsedTime() throws IOException {
        testHelper.givenALocationExists("Cathedral", 137, -137);

        testHelper.givenPartyHasTeleported("Cathedral");

        Document doc = testHelper.getDoc();
        assertThat(doc.body().text()).contains("Day 1, 12AM");
    }
}