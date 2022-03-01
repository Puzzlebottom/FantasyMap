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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    public void givenPartyHasMovedToDestination(int port, String destination, int deltaHours) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("destinationName", destination);
        body.add("deltaHours", String.valueOf(deltaHours));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(getBaseUri(port) + "/log-entries/destination", entity, Object.class);
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

    public List<String> getAllLocationNames(int port) {

        List<Map<String, Object>> result = restTemplate.getForObject(getBaseUri(port) + "/stored-locations", List.class);
        return result.stream()
                .map(obj -> (String) obj.get("name"))
                .collect(toList());
    }
}
