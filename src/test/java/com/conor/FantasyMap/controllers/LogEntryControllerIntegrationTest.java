package com.conor.FantasyMap.controllers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.IOException;

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
}