package com.example.demospring.service;

import com.example.demospring.dto.Todo;
import com.example.demospring.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {

    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public Todo create(String title) {
        return repository.save(new Todo(0, title, false));
    }

    public List<Todo> findAll() {
        return repository.findAll();
    }

    public Todo findById(long id) {
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Todo markDone(long id) {
        Todo existing = findById(id);
        return repository.save(new Todo(existing.id(), existing.title(), true));
    }

    public void delete(long id) {
        if (!repository.deleteById(id)) {
            throw new NoSuchElementException();
        }
    }
}
