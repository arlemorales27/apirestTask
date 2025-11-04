package com.acme.reactivetasks.service;

import com.acme.reactivetasks.domain.Task;
import com.acme.reactivetasks.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Flux<Task> list(long limit, long offset) {
        if (limit <= 0) limit = 50;
        if (offset < 0) offset = 0;
        return repository.findAllPaged(limit, offset);
    }

    public Mono<Task> get(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public Mono<Task> create(String title, Boolean completed) {
        boolean done = completed != null && completed;
        Task toSave = new Task(null, title, done, null, null);
        return repository.save(toSave);
    }

    @Transactional
    public Mono<Task> update(UUID id, String title, Boolean completed) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Task %s no existe".formatted(id))))
                .flatMap(existing -> {
                    boolean newCompleted = completed != null ? completed : existing.completed();
                    String newTitle = title != null ? title : existing.title();
                    // updatedAt será ajustado por trigger en DB; seteamos valor para mantener consistencia local
                    Task updated = new Task(existing.id(), newTitle, newCompleted, existing.createdAt(), Instant.now());
                    return repository.save(updated);
                });
    }

    @Transactional
    public Mono<Void> delete(UUID id) {
        return repository.deleteById(id);
    }

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String msg) { super(msg); }
    }
}
