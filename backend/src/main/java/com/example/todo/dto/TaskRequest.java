package com.example.todo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequest {

    private String title;

    private String status;

    private LocalDateTime completedAt;
}