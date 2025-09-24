package com.emobile.springtodo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoRequestDTO (
    @NotBlank
    String title,

    @Size(max = 1000)
    String description,

    boolean completed){
}
