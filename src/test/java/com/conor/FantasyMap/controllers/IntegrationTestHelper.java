package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.Location;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

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

        HttpHeaders headers = httpHeadersWithAuthorization();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(getBaseUri() + "/log-entries/free", entity, Object.class);
    }

    public void givenPartyHasMovedToDestination(String destination, int deltaHours) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("destinationName", destination);
        body.add("deltaHours", String.valueOf(deltaHours));

        HttpHeaders headers = httpHeadersWithAuthorization();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        restTemplate.postForObject(getBaseUri() + "/log-entries/destination", entity, Object.class);
    }

    public void givenALocationExists(String name, double x, double y) {
        Location location = new Location();
        location.setName(name);
        location.setX(x);
        location.setY(y);

        ResponseEntity<Object> response = exchange("/stored-locations", POST, location);
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
        ResponseEntity<List<Map<String, Object>>> result = exchange("/stored-locations", GET, null);

        return result.getBody().stream()
                .map(obj -> (String) obj.get("name"))
                .collect(toList());
    }

    public <TResult, TRequest> ResponseEntity<TResult> exchange(String uriFragment, HttpMethod httpMethod, TRequest request) {
        HttpEntity<TRequest> requestEntity = new HttpEntity<>(request, httpHeadersWithAuthorization());
        return restTemplate.exchange(getBaseUri() + uriFragment, httpMethod, requestEntity, new ParameterizedTypeReference<>() {});
    }

    private static HttpHeaders httpHeadersWithAuthorization() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", AUTHORIZATION_HEADER);
        return headers;
    }
}
