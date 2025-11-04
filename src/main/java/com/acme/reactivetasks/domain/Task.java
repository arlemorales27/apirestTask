package com.acme.reactivetasks.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidad inmutable usando Java record.
 * Mapeada con Spring Data R2DBC.
 */
@Table("tasks")
public record Task(
        @Id UUID id,
        String title,
        @Column("completed") boolean completed,
        @Column("created_at") Instant createdAt,
        @Column("updated_at") Instant updatedAt
) {}
