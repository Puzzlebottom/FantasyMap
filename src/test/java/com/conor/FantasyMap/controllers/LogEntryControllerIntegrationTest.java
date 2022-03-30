package com.conor.FantasyMap.controllers;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LogEntryControllerIntegrationTest extends IntegrationTest {
    @Test
    void undoMoveShouldDeleteLastLogEntry() throws IOException {
        testHelper.givenPartyHasMoved("N", 8);
        testHelper.givenPartyHasMoved("E", 12);
        testHelper.givenPartyHasMoved("SW", 9);

        restTemplate.exchange("/log-entries/delete-top-entry", HttpMethod.POST, null, String.class);

        Document doc = testHelper.getDoc();
        Elements li = doc.select("li");

        assertThat(li.size()).isEqualTo(2);
        assertThat(li.get(0).text()).isEqualTo("Party travelled north for 8 hours");
        assertThat(li.get(1).text()).isEqualTo("Party travelled east for 12 hours");
    }
}