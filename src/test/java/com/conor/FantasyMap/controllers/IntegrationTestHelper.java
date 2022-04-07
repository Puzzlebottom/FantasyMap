package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class IntegrationTestHelper {

    private TestRestTemplate restTemplate;
    private int port;
    private static final String AUTHORIZATION_HEADER = "Basic " + Base64.getEncoder().encodeToString("test:test".getBytes());

    String getBaseUri() {
        return "http://localhost:" + port;
    }

    public void givenPartyHasMoved(String direction, int deltaHours) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("direction", direction);
        body.add("deltaHours", String.valueOf(deltaHours));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", AUTHORIZATION_HEADER);
        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(getBaseUri() + "/log-entries/free", entity, Object.class);
    }

    public void givenPartyHasMovedToDestination(String destination, int deltaHours) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("destinationName", destination);
        body.add("deltaHours", String.valueOf(deltaHours));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", AUTHORIZATION_HEADER);
        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(getBaseUri() + "/log-entries/destination", entity, Object.class);
    }

    public void givenALocationExists(String name, double x, double y) {
        Location location = new Location();
        location.setName(name);
        location.setX(x);
        location.setY(y);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", AUTHORIZATION_HEADER);

        HttpEntity<Location> entity = new HttpEntity<>(location, headers);

        ResponseEntity<Object> response = restTemplate.exchange(getBaseUri() + "/stored-locations", HttpMethod.POST, entity, Object.class);
        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("call failed");
        }
    }

    public void givenALocationExists(String name) {
        givenALocationExists(name, 0, 0);
    }

    public Document getDoc() throws IOException {
        return Jsoup.connect(getBaseUri())
                .header("Authorization", AUTHORIZATION_HEADER)
                .get();
    }

    public List<String> getAllLocationNames() {

        List<Map<String, Object>> result = restTemplate.getForObject(getBaseUri() + "/stored-locations", List.class);
        return result.stream()
                .map(obj -> (String) obj.get("name"))
                .collect(toList());
    }
}
