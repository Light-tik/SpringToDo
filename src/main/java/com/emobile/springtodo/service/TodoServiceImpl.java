package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TodoRequestDTO;
import com.emobile.springtodo.dto.response.PageResponse;
import com.emobile.springtodo.dto.response.TodoResponseDTO;
import com.emobile.springtodo.entity.TodoEntity;
import com.emobile.springtodo.exception.CustomException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoMapper mapper;

    private final TodoRepository repository;

    private final TodoMetricsService metricsService;

    @Override
    public TodoResponseDTO create(TodoRequestDTO request) {
        TodoEntity entity = mapper.requestToEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);
        log.info("Entity created: {}", entity.getId());
        return mapper.entityToResponse(entity);
    }

    @Cacheable(value = "todos", key = "#id")
    @Override
    public TodoResponseDTO getById(Long id) {
        TodoEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException("Task  " + id +" not found"));
        return mapper.entityToResponse(entity);
    }

    @Override
    public PageResponse<TodoResponseDTO> getAll(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<TodoEntity> todosPage = repository.findAll(pageable);
        List<TodoEntity> entityList = todosPage.getContent();
        List<TodoResponseDTO> response = mapper.listTodoEntityToListTodoResponse(entityList);
        return new PageResponse<>(response, todosPage.getTotalElements());
    }

    @CachePut(value = "todos", key = "#id")
    @Override
    public TodoResponseDTO update(Long id, TodoRequestDTO request) {
        TodoEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException("Task " + id +" not found "));
        entity.setTitle(request.title());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setDescription(request.description());
        repository.save(entity);
        log.info("Entity updated: {}", entity.getId());
        return mapper.entityToResponse(entity);
    }

    @CacheEvict(value = "todos", key = "#id")
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)){
            throw new CustomException("Task " + id +" not found ");
        }
        repository.deleteById(id);
        log.info("Entity deleted: {}", id);
    }

    @CachePut(value = "todos", key = "#id")
    @Override
    public TodoResponseDTO markAsCompleted(Long id) {
        TodoEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomException("Task " + id +" not found "));
        entity.setCompleted(true);
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);
        metricsService.incrementCompleted();
        log.info("Task completed : {}", entity.getId());
        return mapper.entityToResponse(entity);
    }
}
