package com.example.demospring.controller;

import com.example.demospring.dto.Todo;
import com.example.demospring.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService service;

    @Test
    void create_nominal_returns201() throws Exception {
        when(service.create("Buy milk")).thenReturn(new Todo(1L, "Buy milk", false));
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Buy milk\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Buy milk"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void create_blankTitle_returns400() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void create_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void list_nominal_returns200() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Todo(1L, "A", false)));
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("A"));
    }

    @Test
    void get_nominal_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(new Todo(1L, "A", false));
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void get_unknown_returns404() throws Exception {
        when(service.findById(999L)).thenThrow(new NoSuchElementException());
        mockMvc.perform(get("/api/todos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void get_invalidId_returns400() throws Exception {
        mockMvc.perform(get("/api/todos/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void markDone_nominal_returns200() throws Exception {
        when(service.markDone(1L)).thenReturn(new Todo(1L, "A", true));
        mockMvc.perform(put("/api/todos/1/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void markDone_unknown_returns404() throws Exception {
        when(service.markDone(999L)).thenThrow(new NoSuchElementException());
        mockMvc.perform(put("/api/todos/999/done"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void markDone_invalidId_returns400() throws Exception {
        mockMvc.perform(put("/api/todos/abc/done"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void delete_nominal_returns204() throws Exception {
        doNothing().when(service).delete(1L);
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknown_returns404() throws Exception {
        doThrow(new NoSuchElementException()).when(service).delete(999L);
        mockMvc.perform(delete("/api/todos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void delete_invalidId_returns400() throws Exception {
        mockMvc.perform(delete("/api/todos/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
