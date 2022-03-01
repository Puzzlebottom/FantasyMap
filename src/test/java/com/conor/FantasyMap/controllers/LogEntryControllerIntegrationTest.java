package com.conor.FantasyMap.controllers;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

class LogEntryControllerIntegrationTest extends IntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String baseUri;

    @BeforeEach
    void setup() {
        baseUri = "http://localhost:" + port;
    }

    @Test
    @Ignore
    void undoMoveShouldDeleteLastLogEntry() {

    }

}