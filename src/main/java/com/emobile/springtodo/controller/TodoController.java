package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.request.TodoRequestDTO;
import com.emobile.springtodo.dto.response.PageResponse;
import com.emobile.springtodo.dto.response.TodoResponseDTO;
import com.emobile.springtodo.service.TodoService;
import com.emobile.springtodo.swagger.TodoApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController implements TodoApi {

    private final TodoService todoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponseDTO createTodo(@Valid @RequestBody TodoRequestDTO request) {
        return todoService.create(request);
    }

    @GetMapping
    public PageResponse<TodoResponseDTO> getAllTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage) {
        return todoService.getAll(page, perPage);
    }

    @GetMapping("/{id}")
    public TodoResponseDTO getTodo(@PathVariable Long id) {
        return todoService.getById(id);
    }

    @PutMapping("/{id}")
    public TodoResponseDTO updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequestDTO request) {
        return todoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
    }

    @PatchMapping("/{id}")
    public TodoResponseDTO markAsCompleted(@PathVariable Long id) {
        return todoService.markAsCompleted(id);
    }
}
