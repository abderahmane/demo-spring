package com.example.demospring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoRequest(
        @NotBlank @Size(min = 1, max = 100) String title
) {
}
