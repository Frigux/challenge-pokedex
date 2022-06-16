package com.modyo.test.pokedex.service;

import com.modyo.test.pokedex.exceptions.PokemonNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

public class PokedexServiceTests {
    public static MockWebServer mockWebServer;

    private PokedexService pokedexService;

    @Captor
    ArgumentCaptor<String> captor;


    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        pokedexService = new PokedexService(baseUrl);
    }

    @Test
    void getPokemonInformationRequestSuccesful() throws Exception {
        mockWebServer.enqueue(
                    new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"abilities\": [ { \"ability\": { \"name\": \"static\" } } ], \"name\" : \"pikachu\", \"weight\": 60 }")
        );
        pokedexService.getPokemonInformation("pikachu");
        RecordedRequest request = mockWebServer.takeRequest(2000L, TimeUnit.MILLISECONDS);

        assertThat(request.getMethod()).isEqualTo("GET");
    }

    @Test
    void getPokemonInformationNotFound(){
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(404)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                        .setBody("Not Found")
        );

        assertThrows(PokemonNotFoundException.class, () ->
                pokedexService.getPokemonInformation("asd")
        );
    }
}
