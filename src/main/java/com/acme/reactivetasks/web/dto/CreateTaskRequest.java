package com.acme.reactivetasks.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank(message = "title es obligatorio") String title,
        Boolean completed
) {}
