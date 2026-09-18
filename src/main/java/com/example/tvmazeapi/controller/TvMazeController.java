package com.example.tvmazeapi.controller;

import com.example.tvmazeapi.service.TvMazeService;
import com.example.tvmazeapi.service.TvMazeShowService;
import tools.jackson.databind.JsonNode;
import org.bson.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TvMazeController {

    private final TvMazeService tvMazeService;
    private final TvMazeShowService tvMazeShowService;

    public TvMazeController(TvMazeService tvMazeService, TvMazeShowService tvMazeShowService) {
        this.tvMazeService = tvMazeService;
        this.tvMazeShowService = tvMazeShowService;
    }

    @GetMapping("/shows")
    public ResponseEntity<JsonNode> getShows() {
        return ResponseEntity.ok(tvMazeService.getShows());
    }

    @GetMapping("/shows/{id}")
    public ResponseEntity<Document> getShow(@PathVariable int id) {
        return ResponseEntity.ok(tvMazeShowService.getShow(id));
    }
}
