package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.request.TodoRequestDTO;
import com.emobile.springtodo.dto.response.PageResponse;
import com.emobile.springtodo.dto.response.TodoResponseDTO;
import com.emobile.springtodo.entity.TodoEntity;
import com.emobile.springtodo.exception.CustomException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.repository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoMapper mapper;

    @Mock
    private TodoRepository repository;

    @Mock
    private TodoMetricsService metricsService;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    @DisplayName("Create todo")
    void createSuccess() {
        TodoRequestDTO request = new TodoRequestDTO("Task1", "Description", false);

        TodoEntity entity = new TodoEntity();
        entity.setId(1L);
        entity.setTitle("Task1");
        entity.setDescription("Description");

        TodoResponseDTO response = new TodoResponseDTO(
                1L,
                "Task1",
                "Description",
                false,
                LocalDateTime.now(),
                LocalDateTime.now());

        when(mapper.requestToEntity(request)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(response);

        TodoResponseDTO result = todoService.create(request);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Task1");
        verify(repository).save(entity);
        verify(mapper).requestToEntity(request);
        verify(mapper).entityToResponse(entity);
    }

    @Test
    @DisplayName("Fail create todo with null")
    void createFailNotValidArgument() {
        assertThatThrownBy(() -> todoService.create(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Get todo by Id")
    void getByIdSuccess() {
        Long id = 1L;
        TodoEntity entity = new TodoEntity();
        entity.setId(1L);
        entity.setTitle("Test");
        TodoResponseDTO dto = new TodoResponseDTO(id,
                "Test",
                "Desc",
                false,
                LocalDateTime.now(),
                LocalDateTime.now());

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.entityToResponse(entity)).thenReturn(dto);

        TodoResponseDTO result = todoService.getById(id);
        assertThat(result.id()).isEqualTo(id);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Fail get todo by non-existent Id")
    void getByIdFailNotFound() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> todoService.getById(id))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Task " + id +" not found ");
    }

    @Test
    @DisplayName("Get all todos with pagination")
    void getAll() {
            int page = 1;
            int perPage = 10;
            Pageable pageable = PageRequest.of(page - 1, perPage);

            TodoEntity entity1 = new TodoEntity(1L, "Task1", "Desc1", false, null, null);
            TodoEntity entity2 = new TodoEntity(2L, "Task2", "Desc2", false, null, null);
            List<TodoEntity> entityList = List.of(entity1, entity2);

            Page<TodoEntity> pageResult = new PageImpl<>(entityList, pageable, 2);

            when(repository.findAll(pageable)).thenReturn(pageResult);

            when(mapper.listTodoEntityToListTodoResponse(entityList)).thenReturn(List.of(
                    new TodoResponseDTO(1L, "Task1", "Desc1", false, null, null),
                    new TodoResponseDTO(2L, "Task2", "Desc2", false, null, null)
            ));

            PageResponse<TodoResponseDTO> result = todoService.getAll(page, perPage);

            assertThat(result.content().get(0).title()).isEqualTo("Task1");
            assertThat(result.content().get(1).title()).isEqualTo("Task2");

            verify(repository).findAll(pageable);
            verify(mapper).listTodoEntityToListTodoResponse(entityList);
    }

    @Test
    @DisplayName("Update todo")
    void updateSuccess() {
        Long id = 1L;
        TodoRequestDTO request = new TodoRequestDTO("Updated", "Updated Desc", false);

        TodoEntity entity = new TodoEntity();
        entity.setId(id);
        entity.setTitle("Old");
        entity.setDescription("Old Desc");

        TodoResponseDTO responseDTO = new TodoResponseDTO(
                id, "Updated", "Updated Desc", false,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.entityToResponse(entity)).thenReturn(responseDTO);

        TodoResponseDTO result = todoService.update(id, request);

        assertThat(result.title()).isEqualTo("Updated");
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Fail update todo by non-existent Id")
    void updateFailNotFound() {
        Long id = 42L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        TodoRequestDTO request = new TodoRequestDTO("New", "New desc", false);

        assertThatThrownBy(() -> todoService.update(id, request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Task " + id +" not found ");
    }

    @Test
    @DisplayName("Delete todo by Id")
    void deleteSuccess() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        todoService.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Fail delete by non-existent Id")
    void deleteFailNotFound() {
        Long id = 999L;
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> todoService.delete(id))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Task " + id +" not found ");
    }

    @Test
    @DisplayName("Mark a todo as completed")
    void markAsCompletedSuccess() {
        Long id = 1L;
        TodoEntity entity = new TodoEntity();
        entity.setId(id);
        entity.setTitle("Test");
        entity.setCompleted(false);

        TodoResponseDTO dto = new TodoResponseDTO(
                id, "Test", "Desc", true,
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.entityToResponse(entity)).thenReturn(dto);

        TodoResponseDTO result = todoService.markAsCompleted(id);

        assertThat(result.completed()).isTrue();
        verify(repository).save(entity);
        verify(metricsService).incrementCompleted();
    }

    @Test
    @DisplayName("Fail mark a todo by non-existent Id as completed")
    void markAsCompletedFailNotFound() {
        Long id = 404L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.markAsCompleted(id))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Task " + id +" not found ");
    }
}
