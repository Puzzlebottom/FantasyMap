package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;

@Component
public class IntegrationTestHelper {

    @Autowired
    private TestRestTemplate restTemplate;

    String getBaseUri(int port) {
        return "http://localhost:" + port;
    }
    public void givenPartyHasMoved(int port, String direction, int deltaHours) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("direction", direction);
        body.add("deltaHours", String.valueOf(deltaHours));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(getBaseUri(port) + "/log-entries/free", entity, Object.class);
    }


    public void givenALocationExists(int port, String name, int xCoord, int yCoord) {
        Location location = new Location();
        location.setName(name);
        location.setXCoord(xCoord);
        location.setYCoord(yCoord);

        restTemplate.postForObject(getBaseUri(port) + "/stored-locations", location, Object.class);
    }

    public void givenALocationExists(int port, String name) {
        givenALocationExists(port, name, 0, 0);
    }

    public Document getDoc(int port) throws IOException {
        return Jsoup.connect(getBaseUri(port)).get();
    }
}
