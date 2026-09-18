package com.example.tvmazeapi.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TvMazeService {

    private final RestClient tvMazeRestClient;
    private final MongoTemplate mongoTemplate;

    public TvMazeService(RestClient tvMazeRestClient, MongoTemplate mongoTemplate) {
        this.tvMazeRestClient = tvMazeRestClient;
        this.mongoTemplate = mongoTemplate;
    }

    public JsonNode getShows() {
        JsonNode shows = tvMazeRestClient.get()
                .uri("/shows")
                .retrieve()
                .body(JsonNode.class);

        addComments(shows);
        return shows;
    }

    private void addComments(JsonNode shows) {
        if (shows == null || !shows.isArray()) {
            return;
        }

        Map<Integer, List<Document>> commentsByShowId = new HashMap<>();
        for (Document cachedShow : mongoTemplate.findAll(Document.class, "shows")) {
            Integer showId = cachedShow.getInteger("_id");
            if (showId != null) {
                commentsByShowId.put(
                        showId,
                        cachedShow.getList("comments", Document.class, List.of()));
            }
        }

        for (JsonNode show : shows) {
            if (!(show instanceof ObjectNode showObject)) {
                continue;
            }

            ArrayNode comments = showObject.putArray("comments");
            JsonNode showId = showObject.get("id");
            if (showId == null || !showId.isInt()) {
                continue;
            }

            for (Document comment : commentsByShowId.getOrDefault(showId.intValue(), List.of())) {
                ObjectNode commentObject = comments.addObject();
                commentObject.put("comment", comment.getString("comment"));
                commentObject.put("rating", comment.getInteger("rating"));
            }
        }
    }
}
