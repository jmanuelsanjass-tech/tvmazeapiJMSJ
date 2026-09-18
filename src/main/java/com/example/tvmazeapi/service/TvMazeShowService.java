package com.example.tvmazeapi.service;

import org.bson.Document;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TvMazeShowService {

    private static final String COLLECTION_NAME = "shows";

    private final MongoTemplate mongoTemplate;
    private final RestClient tvMazeRestClient;
    public TvMazeShowService(
            MongoTemplate mongoTemplate,
            RestClient tvMazeRestClient) {
        this.mongoTemplate = mongoTemplate;
        this.tvMazeRestClient = tvMazeRestClient;
    }

    public Document getShow(int id) {
        Document cachedShow = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(id)),
                Document.class,
                COLLECTION_NAME);

        if (cachedShow != null) {
            return cachedShow;
        }

        Document show = fetchShow(id);
        Document cachedDocument = new Document(show)
                .append("_id", id)
                .append("comments", List.of());
        mongoTemplate.save(cachedDocument, COLLECTION_NAME);
        return cachedDocument;
    }

    private Document fetchShow(int id) {
        try {
            String response = tvMazeRestClient.get()
                    .uri("/shows/{id}", id)
                    .retrieve()
                    .body(String.class);
            return Document.parse(response);
        } catch (DataAccessException exception) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible consultar el cache de MongoDB",
                    exception);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "TVMaze devolvió una respuesta inválida",
                    exception);
        }
    }
}
