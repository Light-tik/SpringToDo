package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TodoRequestDTO;
import com.emobile.springtodo.dto.response.PageResponse;
import com.emobile.springtodo.dto.response.TodoResponseDTO;

public interface TodoService {
    TodoResponseDTO create(TodoRequestDTO request);
    TodoResponseDTO getById(Long id);
    PageResponse<TodoResponseDTO> getAll(int page, int perPage);
    TodoResponseDTO update(Long id, TodoRequestDTO request);
    void delete(Long id);
    TodoResponseDTO markAsCompleted(Long id);
}
