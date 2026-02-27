package com.castores.backend.controller;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
public ResponseEntity<Map<String, Object>> health() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "OK");
    response.put("message", "API Inventario Castores funcionando");
    response.put("timestamp", LocalDateTime.now());
    return ResponseEntity.ok(response);
}
}