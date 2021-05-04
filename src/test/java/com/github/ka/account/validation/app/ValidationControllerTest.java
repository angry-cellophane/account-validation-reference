package com.github.ka.account.validation.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ka.account.validation.app.model.Request;
import com.github.ka.account.validation.app.model.Responses;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import spark.Spark;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("localTest")
class ValidationControllerTest {

    static final ObjectMapper JSON = new ObjectMapper();

    @Autowired
    TestRestTemplate rest;

    @BeforeAll
    static void setup() throws IOException {
        var valid = validResponse();
        var nonValid = nonValidResponse();


        Spark.port(9090);
        Spark.post("/api1", (req, resp) -> {
            resp.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return valid;
        });
        Spark.post("/api2", (req, resp) -> {
            resp.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            return nonValid;
        });
    }

    @AfterAll
    static void cleanup() throws IOException {
        Spark.awaitStop();
    }

    @Test
    public void validResponseOnValidRequest() {
        var request = new Request();
        request.setProviders(List.of("provider1"));
        request.setAccountNumber("1");
        request.setSortCode("1");

        var response = rest.postForEntity("/api/v1/validate", request, Responses.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        var responses = response.getBody().getResponses();
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("provider1", responses.get(0).getProvider());
        assertTrue(responses.get(0).isValid());
    }

    @Test
    public void invalidResponseFromValidRequest() {
        var request = new Request();
        request.setProviders(List.of("provider2"));
        request.setAccountNumber("1");
        request.setSortCode("1");

        var response = rest.postForEntity("/api/v1/validate", request, Responses.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        var responses = response.getBody().getResponses();
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("provider2", responses.get(0).getProvider());
        assertFalse(responses.get(0).isValid());
    }

    @Test
    public void multipleResponseFromMultipleProviders() {
        var request = new Request();
        request.setProviders(List.of("provider1", "provider2"));
        request.setAccountNumber("1");
        request.setSortCode("1");

        var response = rest.postForEntity("/api/v1/validate", request, Responses.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        var responses = response.getBody().getResponses();
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    public void allProvidersUsedWhenProvidersListIsEmpty() {
        var request = new Request();
        request.setProviders(Collections.emptyList());
        request.setAccountNumber("1");
        request.setSortCode("1");

        var response = rest.postForEntity("/api/v1/validate", request, Responses.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        var responses = response.getBody().getResponses();
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    public void allProvidersUsedWhenProvidersListIsNull() {
        var request = new Request();
        request.setProviders(null);
        request.setAccountNumber("1");
        request.setSortCode("1");

        var response = rest.postForEntity("/api/v1/validate", request, Responses.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        var responses = response.getBody().getResponses();
        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @SneakyThrows
    static String toString(Responses.Response response) {
        return JSON.writeValueAsString(response);
    }

    static String validResponse() {
        var response = new Responses.Response();
        response.setProvider("provider1");
        response.setValid(true);
        return toString(response);
    }

    static String nonValidResponse() {
        var response = new Responses.Response();
        response.setProvider("provider2");
        response.setValid(false);
        return toString(response);
    }
}