package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Map<String, Object> getTasksResponse() {

        List<Task> tasks = taskRepository.findAll();

        long activeCount = tasks.stream()
                .filter(task -> "active".equals(task.getStatus()))
                .count();

        long completeCount = tasks.stream()
                .filter(task -> "complete".equals(task.getStatus()))
                .count();

        Map<String, Object> response = new HashMap<>();

        response.put("tasks", tasks);
        response.put("activeCount", activeCount);
        response.put("completeCount", completeCount);

        return response;
    }

    public Task create(TaskRequest request) {

        Task task = Task.builder()
                .title(request.getTitle())
                .status(request.getStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return taskRepository.save(task);
    }

    public Task update(Long id, TaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null &&
                !request.getTitle().trim().isEmpty()) {
            task.setTitle(request.getTitle());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        task.setCompletedAt(request.getCompletedAt());

        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}