package com.emobile.springtodo;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Profile("test")
class ControllerTodoIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);

    @Autowired
    private MockMvc mockMvc;

    private EntityManager entityManager;

    @BeforeEach
    void resetDb(TestInfo info) {
        if (info.getTags().contains("truncate")) {
            entityManager.createNativeQuery("TRUNCATE TABLE todos RESTART IDENTITY CASCADE").executeUpdate();
        }
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));

    }

    @Test
    @Tag("truncate")
    @DisplayName("POST api/todo/ - create todo")
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createTodo_success() throws Exception{
        String requestBody = """
                {"title":"Task1",
                "description":"Task create",
                "completed":false}
                """;
        String expectedJson = """
                {"title":"Task1",
                "description":"Task create",
                "completed":false}
                """;
        MvcResult result = mockMvc.perform(post("/api/todo")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, responseBody, JSONCompareMode.LENIENT);
    }

    @Test
    @DisplayName("Fail POST /api/todo - create todo with null")
    void createTodo_shouldFailValidation() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/todo/{id} - get todo by Id")
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getTodoById_success() throws Exception{
    mockMvc.perform(get("/api/todo/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Test Task 1"));
    }

    @Test
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Fail GET /api/todo - Get todo by non-existent Id")
    void getTodoById_FailedNotFound() throws Exception{
        mockMvc.perform(get("/api/todo/999"))
                .andExpect(status().isNotFound());  }

    @Test
    @DisplayName("UPDATE /api/todo/{id} - update todo by Id")
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateTodo_success() throws Exception{
        String updatedRequest = """
                {"title":"Updated Task",
                "description":"Task update",
                "completed":false}
                """;
        mockMvc.perform(put("/api/todo/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Task update"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("DELETE /api/todo/{id} - Delete todo by Id")
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteTodo_success() throws Exception{
        mockMvc.perform(delete("/api/todo/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/todo - Get todo with pagination")
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getTodosWithPagination_success() throws Exception{
        mockMvc.perform(get("/api/todo?page=1&perPage=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Test Task 1"))
                .andExpect(jsonPath("$.content[1].title").value("Test Task 2"));
    }

    @Test
    @Sql(scripts = "/sql/insert_todos.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("PATCH /api/todo/{id} - mark todo as completed")
    void markAsCompleted_success() throws Exception{
        mockMvc.perform(patch("/api/todo/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.completed").value(true))
        .andExpect(jsonPath("$.title").value("Test Task 1"));
    }
}
