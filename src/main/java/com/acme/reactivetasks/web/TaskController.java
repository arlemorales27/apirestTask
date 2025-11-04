package com.acme.reactivetasks.web;

import com.acme.reactivetasks.domain.Task;
import com.acme.reactivetasks.service.TaskService;
import com.acme.reactivetasks.web.dto.CreateTaskRequest;
import com.acme.reactivetasks.web.dto.TaskResponse;
import com.acme.reactivetasks.web.dto.UpdateTaskRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<TaskResponse> list(@RequestParam(defaultValue = "50") long limit,
                                   @RequestParam(defaultValue = "0") long offset) {
        return service.list(limit, offset).map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<TaskResponse> get(@PathVariable UUID id) {
        return service.get(id).map(this::toResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> create(@Valid @RequestBody CreateTaskRequest body) {
        return service.create(body.title(), body.completed()).map(this::toResponse);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TaskResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateTaskRequest body) {
        return service.update(id, body.title(), body.completed()).map(this::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return service.delete(id);
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(t.id(), t.title(), t.completed(), t.createdAt(), t.updatedAt());
    }
}
