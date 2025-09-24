package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.TodoEntity;
import com.emobile.springtodo.mapper.TodoRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepository{

    private final JdbcTemplate jdbcTemplate;

    private final TodoRowMapper rowMapper;

    @Override
    public void save(TodoEntity todo) {
        todo.setDescription(todo.getDescription() != null ? todo.getDescription() : "");
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        String sql = "INSERT INTO todos (title, description, completed, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        Long generatedId = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
        );

        todo.setId(generatedId);
    }

    @Override
    public Optional<TodoEntity> findById(Long id) {
        String sql = "SELECT * FROM todos WHERE id = ?";
        List<TodoEntity> result = jdbcTemplate.query(sql, new TodoRowMapper(), id);
        return result.stream().findFirst();
    }

    @Override
    public Page<TodoEntity> findAll(Pageable pageable) {
        String sql = "SELECT * FROM todos ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<TodoEntity> content = jdbcTemplate.query(
                sql,
                rowMapper,
                pageable.getPageSize(),
                pageable.getOffset()
        );
        String countSql = "SELECT COUNT(*) FROM todos";
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public void update(TodoEntity entity) {
        String sql = "UPDATE todos SET title = ?, description = ?, completed = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                entity.getTitle(),
                entity.getDescription(),
                entity.isCompleted(),
                entity.getUpdatedAt(),
                entity.getId());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM  todos WHERE id = ?", id);
    }
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM todos WHERE id = ?)";
        Boolean exists = jdbcTemplate.queryForObject(sql, Boolean.class, id);
        return Boolean.TRUE.equals(exists);
    }
}
