package com.example.demospring.controller;

import com.example.demospring.dto.Todo;
import com.example.demospring.dto.TodoRequest;
import com.example.demospring.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody TodoRequest request) {
        Todo created = service.create(request.title());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Todo> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Todo get(@PathVariable long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}/done")
    public Todo markDone(@PathVariable long id) {
        return service.markDone(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
