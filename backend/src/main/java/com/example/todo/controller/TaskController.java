package com.example.todo.controller;

import com.example.todo.dto.TaskRequest;
import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {

        // Tạo đối tượng Pageable sắp xếp theo ngày tạo mới nhất lên đầu (createdAt giảm
        // dần)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // Gọi hàm phân trang từ Service
        return ResponseEntity.ok(taskService.getTasksPagedResponse(pageable));
    }

    @PostMapping
    public Task create(
            @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @PutMapping("/{id}")
    public Task update(
            @PathVariable Long id,
            @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {
        taskService.delete(id);
    }
}