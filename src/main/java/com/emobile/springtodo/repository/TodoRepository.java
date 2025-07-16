package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TodoRepository {
    void save(TodoEntity entity);
    Optional<TodoEntity> findById(Long id);
    Page<TodoEntity> findAll(Pageable pageable);
    void update(TodoEntity entity);
    void deleteById(Long id);
    boolean existsById(Long id);
}
