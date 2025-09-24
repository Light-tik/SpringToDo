package com.emobile.springtodo.swagger;

import com.emobile.springtodo.dto.request.TodoRequestDTO;
import com.emobile.springtodo.dto.response.PageResponse;
import com.emobile.springtodo.dto.response.TodoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/todos")
@Tag(name = "TODOs", description = "Operations with TODO items")
public interface TodoApi {

    @Operation(summary = "Create a new TODO", description = "Creates a new TODO item with title and due date.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "TODO created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping
    TodoResponseDTO createTodo(@Valid @RequestBody TodoRequestDTO request);

    @Operation(summary = "Get all TODOs", description = "Returns paginated list of TODOs")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found TODO")
    })
    @GetMapping
    PageResponse<TodoResponseDTO> getAllTodos(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    );

    @Operation(summary = "Get a TODO by ID", description = "Returns a single TODO by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found TODO"),
            @ApiResponse(responseCode = "404", description = "TODO not found")
    })
    @GetMapping("/{id}")
    TodoResponseDTO getTodo(@PathVariable Long id);

    @Operation(summary = "Update a TODO", description = "Updates a TODO by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO updated"),
            @ApiResponse(responseCode = "404", description = "TODO not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    TodoResponseDTO updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequestDTO request);

    @Operation(summary = "Delete a TODO", description = "Deletes a TODO by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "TODO deleted"),
            @ApiResponse(responseCode = "404", description = "TODO not found")
    })
    @DeleteMapping("/{id}")
    void deleteTodo(@PathVariable Long id);

    @Operation(summary = "Mark a TODO as completed", description = "Marks the TODO as completed by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "TODO marked as completed"),
            @ApiResponse(responseCode = "404", description = "TODO not found")
    })
    @PatchMapping("/{id}")
    TodoResponseDTO markAsCompleted(@PathVariable Long id);
}
