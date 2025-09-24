package com.emobile.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todos")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq")
    @SequenceGenerator(name = "todo_seq", sequenceName = "todo_id_seq", allocationSize = 1)
    private Long id;

    private String title;

    private String description;

    private boolean completed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
