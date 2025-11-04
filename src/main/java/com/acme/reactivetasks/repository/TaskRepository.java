package com.acme.reactivetasks.repository;

import com.acme.reactivetasks.domain.Task;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.Param;

import reactor.core.publisher.Flux;

import java.util.UUID;

public interface TaskRepository extends ReactiveCrudRepository<Task, UUID> {

    @Query("SELECT * FROM tasks ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    Flux<Task> findAllPaged(@Param("limit") long limit, @Param("offset") long offset);
}
