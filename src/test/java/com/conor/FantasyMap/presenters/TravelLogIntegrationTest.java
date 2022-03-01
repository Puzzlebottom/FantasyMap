package com.conor.FantasyMap.presenters;

import com.conor.FantasyMap.controllers.IntegrationTestHelper;
import com.conor.FantasyMap.models.Location;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TravelLogIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    @Test
    void shouldContainListOfMovements() throws IOException {
        integrationTestHelper.givenALocationExists(port, "Bastion");
        integrationTestHelper.givenPartyHasMoved(port, "N", 24);
        integrationTestHelper.givenPartyHasMoved(port, "SE", 24);

        Document doc = integrationTestHelper.getDoc(port);
        Elements li = doc.select("li");

        assertThat(li.size()).isEqualTo(2);
        assertThat(li.get(0).text()).isEqualTo("Party travelled north for 24 hours");
    }

    @Test
    @DirtiesContext
    void shouldShowMoveDestination() throws IOException {
        integrationTestHelper.givenALocationExists(port, "Bastion", 1, 1);
        List<String> allLocationNames = integrationTestHelper.getAllLocationNames(port);
        integrationTestHelper.givenPartyHasMovedToDestination(port, allLocationNames.get(0), 24);

        Document doc = integrationTestHelper.getDoc(port);
        Elements li = doc.select("li");

        assertThat(li.size()).isEqualTo(1);
        assertThat(li.get(0).text()).isEqualTo("Party travelled 24 hours toward DESTINATION");
    }
}
