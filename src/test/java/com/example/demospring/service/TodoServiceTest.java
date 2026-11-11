package com.example.demospring.service;

import com.example.demospring.dto.Todo;
import com.example.demospring.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoServiceTest {

    private TodoRepository repository;
    private TodoService service;

    @BeforeEach
    void setUp() {
        repository = new TodoRepository();
        service = new TodoService(repository);
    }

    @Test
    void create_returnsTodoWithDoneFalse() {
        Todo todo = service.create("Buy milk");
        assertThat(todo.id()).isGreaterThan(0);
        assertThat(todo.title()).isEqualTo("Buy milk");
        assertThat(todo.done()).isFalse();
    }

    @Test
    void findAll_returnsCreatedTodos() {
        service.create("A");
        service.create("B");
        List<Todo> all = service.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void findById_existing_returnsTodo() {
        Todo created = service.create("A");
        Todo found = service.findById(created.id());
        assertThat(found).isEqualTo(created);
    }

    @Test
    void findById_unknown_throwsNoSuchElementException() {
        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void markDone_returnsNewTodoWithDoneTrueAndUpdatesStored() {
        Todo created = service.create("A");
        Todo done = service.markDone(created.id());
        assertThat(done.id()).isEqualTo(created.id());
        assertThat(done.title()).isEqualTo("A");
        assertThat(done.done()).isTrue();
        Todo stored = service.findById(created.id());
        assertThat(stored.done()).isTrue();
    }

    @Test
    void delete_existing_removesTodo() {
        Todo created = service.create("A");
        service.delete(created.id());
        assertThatThrownBy(() -> service.findById(created.id()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void delete_unknown_throwsNoSuchElementException() {
        assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
