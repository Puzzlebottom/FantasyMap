package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.controllers.IntegrationTest;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TravelLogIntegrationTest extends IntegrationTest {

    @Test
    void shouldContainListOfMovements() throws IOException {
        testHelper.givenALocationExists("Bastion");
        testHelper.givenPartyHasMoved("N", 24);
        testHelper.givenPartyHasMoved("SE", 24);

        Document doc = testHelper.getDoc();
        Elements entries = doc.getElementsByAttributeValue("data-test-id", "travel-log-entry");

        assertThat(entries.size()).isEqualTo(2);
        assertThat(entries.get(0).text()).isEqualTo("Party travelled north for 24 hours");
    }

    @Test
    void shouldShowMoveDestination() throws IOException {
        testHelper.givenALocationExists("Bastion", 1, 1);
        List<String> allLocationNames = testHelper.getAllLocationNames();
        testHelper.givenPartyHasMovedToDestination(allLocationNames.get(0), 24);

        Document doc = testHelper.getDoc();
        Elements entries = doc.getElementsByAttributeValue("data-test-id", "travel-log-entry");

        assertThat(entries.size()).isEqualTo(1);
        assertThat(entries.get(0).text()).isEqualTo("Party travelled 24 hours toward Bastion");
    }
}
