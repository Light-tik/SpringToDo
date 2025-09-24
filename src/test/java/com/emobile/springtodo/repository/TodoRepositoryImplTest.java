package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.TodoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryImplTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TodoRepository todoRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    void resetDb() {
        em.createNativeQuery("TRUNCATE TABLE todos RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    @DisplayName("Save todo and find by id")
    void testSaveAndFind() {
        TodoEntity todo = new TodoEntity();
        todo.setTitle("Test");
        todo.setDescription("Desc");
        todoRepository.save(todo);
        log.info("Todo with title {} save in repository", todo.getTitle());

        Optional<TodoEntity> found = todoRepository.findById(todo.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Update todo and save")
    void update() {
        TodoEntity todo = new TodoEntity();
        todoRepository.save(todo);

        todo.setTitle("Updated Title");
        todo.setCompleted(true);

        todoRepository.save(todo);
        log.info("Todo update");

        Optional<TodoEntity> updated = todoRepository.findById(todo.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getTitle()).isEqualTo("Updated Title");
        assertThat(updated.get().isCompleted()).isTrue();
    }

    @Test
    @DisplayName("Delete todo")
    void deleteById() {
        TodoEntity todo = new TodoEntity();
        todoRepository.save(todo);

        todoRepository.deleteById(todo.getId());

        Optional<TodoEntity> deleted = todoRepository.findById(todo.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    @DisplayName("Check if such a todo exists")
    void existsById() {
        TodoEntity todo = new TodoEntity();
        todo.setTitle("Test Todo");
        todo.setDescription("Description");
        todo.setCompleted(false);
        todoRepository.save(todo);

        boolean exists = todoRepository.existsById(todo.getId());
        assertThat(exists).isTrue();

        boolean notExists = todoRepository.existsById(9999L);
        assertThat(notExists).isFalse();
    }
}
