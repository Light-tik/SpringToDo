package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.request.TodoRequestDTO;
import com.emobile.springtodo.dto.response.TodoResponseDTO;
import com.emobile.springtodo.entity.TodoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TodoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TodoEntity requestToEntity(TodoRequestDTO request);

    TodoResponseDTO entityToResponse(TodoEntity entity);

    List<TodoResponseDTO> listTodoEntityToListTodoResponse(List<TodoEntity> entityList);
}
