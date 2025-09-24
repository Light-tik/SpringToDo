package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.TodoEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
@Testcontainers
class TodoRepositoryImplTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    TodoRepository todoRepository;

    TodoEntity todo;

    @BeforeEach
    void setUp() {
        todo = new TodoEntity();
        todo.setTitle("Test Title");
        todo.setDescription("Test Description");
        todo.setCompleted(false);
        log.info("Todo create with title: {}", todo.getTitle());
    }

    @Test
    @DisplayName("Save todo and find by id")
    void saveAndFindById() {
        todoRepository.save(todo);
        log.info("Todo with title {} save in repository", todo.getTitle());

        assertThat(todo.getId()).isNotNull();

        Optional<TodoEntity> found = todoRepository.findById(todo.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("Update todo and save")
    void update() {
        todoRepository.save(todo);

        todo.setTitle("Updated Title");
        todo.setCompleted(true);

        todoRepository.update(todo);
        log.info("Todo update");

        Optional<TodoEntity> updated = todoRepository.findById(todo.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("Updated Title");
        assertThat(updated.get().isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Delete todo")
    void deleteById() {
        todoRepository.save(todo);

        todoRepository.deleteById(todo.getId());

        Optional<TodoEntity> deleted = todoRepository.findById(todo.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    @DisplayName("Check if such a todo exists")
    void existsById() {
        todoRepository.save(todo);

        boolean exists = todoRepository.existsById(todo.getId());
        assertThat(exists).isTrue();

        boolean notExists = todoRepository.existsById(9999L);
        assertThat(notExists).isFalse();
    }
}
