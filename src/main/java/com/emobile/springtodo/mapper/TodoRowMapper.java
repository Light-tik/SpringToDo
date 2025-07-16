package com.emobile.springtodo.mapper;

import com.emobile.springtodo.entity.TodoEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TodoRowMapper implements RowMapper<TodoEntity> {

    @Override
    public TodoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        TodoEntity todo = new TodoEntity();
        todo.setId(rs.getLong("id"));
        todo.setTitle(rs.getString("title"));
        todo.setDescription(rs.getString("description"));
        todo.setCompleted(rs.getBoolean("completed"));
        todo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        todo.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return todo;
    }
}
