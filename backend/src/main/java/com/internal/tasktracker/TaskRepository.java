package com.internal.tasktracker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Search tasks by term and optional status filter
    @Query(value = "SELECT * FROM tasks \n" +
        "WHERE archived = FALSE \n" +
        "AND (\n" +
        "    LOWER(title) LIKE :term \n" +
        "    OR LOWER(description) LIKE :term\n" +
        ")\n" +
        "AND (:status IS NULL OR status = :status)\n" +
        "ORDER BY created_at DESC",
           nativeQuery = true)
    List<Task> searchTasks(@Param("term") String term, @Param("status") String status);
}
