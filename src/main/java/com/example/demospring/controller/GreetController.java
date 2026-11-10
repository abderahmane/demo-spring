package com.example.demospring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class GreetController {

    private static final Pattern ALLOWED_NAME = Pattern.compile("^[\\p{L} -]+$");

    @GetMapping("/greet")
    public ResponseEntity<Map<String, String>> greet(@RequestParam(required = false) String name) {
        if (name == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "name is required"));
        }
        if (name.length() < 2 || name.length() > 30) {
            return ResponseEntity.badRequest().body(Map.of("error", "name must be between 2 and 30 characters"));
        }
        if (!ALLOWED_NAME.matcher(name).matches()) {
            return ResponseEntity.badRequest().body(Map.of("error", "name must contain only letters, spaces and hyphens"));
        }
        return ResponseEntity.ok(Map.of("message", "Bonjour " + name));
    }
}
