package com.example.tvmazeapi.controller;

import com.example.tvmazeapi.service.TvMazeService;
import tools.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TvMazeController {

    private final TvMazeService tvMazeService;

    public TvMazeController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }

    @GetMapping("/shows")
    public ResponseEntity<JsonNode> getShows() {
        return ResponseEntity.ok(tvMazeService.getShows());
    }
}
