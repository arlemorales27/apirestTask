package com.acme.reactivetasks.web.dto;

public record UpdateTaskRequest(
        String title,
        Boolean completed
) {}
