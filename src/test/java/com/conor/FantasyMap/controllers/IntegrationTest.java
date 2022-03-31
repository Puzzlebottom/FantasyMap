package com.conor.FantasyMap.controllers;


import com.conor.FantasyMap.repositories.LocationRepository;
import com.conor.FantasyMap.repositories.LogEntryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class IntegrationTest {
    @Autowired
    private LogEntryRepository logEntryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected IntegrationTestHelper testHelper;

    @BeforeEach
    public void setupHelper() {
        testHelper = new IntegrationTestHelper(restTemplate, port);
    }

    @AfterEach
    @Transactional
    public void cleanupDatabase() {
        logEntryRepository.deleteAll();
        locationRepository.deleteAll();
    }
}
