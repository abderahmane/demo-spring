package com.example.demospring.repository;

import com.example.demospring.dto.Todo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TodoRepository {

    private final ConcurrentHashMap<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Todo save(Todo todo) {
        long id = todo.id() == 0 ? sequence.incrementAndGet() : todo.id();
        Todo stored = new Todo(id, todo.title(), todo.done());
        store.put(id, stored);
        return stored;
    }

    public Optional<Todo> findById(long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Todo> findAll() {
        return List.copyOf(store.values());
    }

    public boolean deleteById(long id) {
        return store.remove(id) != null;
    }
}
