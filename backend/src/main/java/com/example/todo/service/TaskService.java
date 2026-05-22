package com.example.todo.service;

import com.example.todo.dto.TaskRequest;
import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Map<String, Object> getTasksPagedResponse(Pageable pageable) {
        // 1. Lấy dữ liệu phân trang từ Database
        Page<Task> taskPage = taskRepository.findAll(pageable);

        // 2. Đếm số lượng active/complete dựa trên TOÀN BỘ database (hoặc dựa trên
        // trang hiện tại tùy bạn)
        List<Task> allTasks = taskRepository.findAll();
        long activeCount = allTasks.stream().filter(task -> "active".equals(task.getStatus())).count();
        long completeCount = allTasks.stream().filter(task -> "complete".equals(task.getStatus())).count();

        // 3. Gom dữ liệu trả về cho Frontend
        Map<String, Object> response = new HashMap<>();
        response.put("tasks", taskPage.getContent()); // Danh sách task của TRANG HIỆN TẠI
        response.put("currentPage", taskPage.getNumber() + 1); // Trang hiện tại (bắt đầu từ số 0)
        response.put("totalItems", taskPage.getTotalElements()); // Tổng số lượng task đang có
        response.put("totalPages", taskPage.getTotalPages()); // Tổng số trang tính được
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