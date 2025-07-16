package com.emobile.springtodo.dto.response;

import java.time.LocalDateTime;

public record TodoResponseDTO (
    Long id,
    String title,
    String description,
    boolean completed,
    LocalDateTime createdAt,
    LocalDateTime updatedAt){
}
