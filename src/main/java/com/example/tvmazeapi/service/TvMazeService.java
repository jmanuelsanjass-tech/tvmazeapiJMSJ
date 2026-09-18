package com.example.tvmazeapi.service;

import tools.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TvMazeService {

    private final RestClient tvMazeRestClient;

    public TvMazeService(RestClient tvMazeRestClient) {
        this.tvMazeRestClient = tvMazeRestClient;
    }

    public JsonNode getShows() {
        return tvMazeRestClient.get()
                .uri("/shows")
                .retrieve()
                .body(JsonNode.class);
    }
}
