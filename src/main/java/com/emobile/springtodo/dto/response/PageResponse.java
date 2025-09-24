package com.emobile.springtodo.dto.response;

import java.util.List;

public record PageResponse<T> (
        List<T> content,

        Long numberOfElements
){}
