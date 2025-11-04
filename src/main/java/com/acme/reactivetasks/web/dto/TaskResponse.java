package com.acme.reactivetasks.web.dto;

import java.time.Instant;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        boolean completed,
        Instant createdAt,
        Instant updatedAt
) {}
