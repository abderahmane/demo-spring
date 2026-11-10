package com.example.demospring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GreetController.class)
class GreetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getGreet_nominal_returns200JsonWithMessage() throws Exception {
        mockMvc.perform(get("/api/greet").param("name", "Awa"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Bonjour Awa"));
    }

    @Test
    void getGreet_missingName_returns400WithError() throws Exception {
        mockMvc.perform(get("/api/greet"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getGreet_singleChar_returns400() throws Exception {
        mockMvc.perform(get("/api/greet").param("name", "A"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getGreet_31Chars_returns400() throws Exception {
        String name = "A".repeat(31);
        mockMvc.perform(get("/api/greet").param("name", name))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getGreet_script_returns400() throws Exception {
        mockMvc.perform(get("/api/greet").param("name", "<script>"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getGreet_emptyName_returns400() throws Exception {
        mockMvc.perform(get("/api/greet").param("name", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getGreet_twoChars_returns200() throws Exception {
        mockMvc.perform(get("/api/greet").param("name", "Ab"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bonjour Ab"));
    }

    @Test
    void getGreet_30Chars_returns200() throws Exception {
        String name = "A".repeat(30);
        mockMvc.perform(get("/api/greet").param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bonjour " + name));
    }

    @Test
    void getGreet_nameWithSpaceAndHyphen_returns200() throws Exception {
        mockMvc.perform(get("/api/greet").param("name", "Anne-Marie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bonjour Anne-Marie"));
    }

    @Test
    void getGreet_errorBody_doesNotContainStackTraceOrJavaClassName() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/greet"))
                .andExpect(status().isBadRequest())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        assertThat(body).doesNotContain("Exception");
        assertThat(body).doesNotContain("StackTrace");
        assertThat(body).doesNotContain("at com.");
        assertThat(body).doesNotContain("java.lang");
        assertThat(body).doesNotContain("GreetController");
    }
}
