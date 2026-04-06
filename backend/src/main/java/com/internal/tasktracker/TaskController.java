package com.internal.tasktracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
        @RequestParam(required = false, defaultValue = "") String q,
        @RequestParam(required = false) String status,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Normalize query input
        String query = (q == null) ? "" : q.trim();

        String searchTerm = null;
        if (!query.isBlank()) {
            searchTerm = "%" + query.toLowerCase() + "%";
        }

        // Logging (CORRECT PLACE)
        logger.info("Search query: {}, status: {}, page: {}, pageSize: {}", query, status, page, pageSize);

        // Parse status filter
        String normalizedStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                normalizedStatus = TaskStatus.valueOf(status.toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                normalizedStatus = null;
            }
        }

        List<Task> allResults = taskRepository.searchTasks(searchTerm, normalizedStatus);

        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, allResults.size());

        List<Task> pageResults = (start < allResults.size())
            ? allResults.subList(start, end)
            : Collections.emptyList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", pageResults);
        response.put("total", allResults.size());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }
}
