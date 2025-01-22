package com.ilker.task_service.repository;

import com.ilker.task_service.entitiy.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByAssignedUserId(Long userId);
}
