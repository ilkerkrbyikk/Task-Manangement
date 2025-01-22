package com.ilker.task_service.service;

import com.ilker.task_service.entitiy.Task;
import com.ilker.task_service.enums.TaskStatus;
import com.ilker.task_service.exception.NotAuthorizedException;
import com.ilker.task_service.exception.TaskNotFoundException;
import com.ilker.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task,String requesterRole){
        if(!requesterRole.equals("ROLE_ADMIN")){
            throw new NotAuthorizedException("Only admin can create a task.");
        }
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task can not found with given ID: " + id));
    }

    public List<Task> getAllTask(TaskStatus taskStatus){
        List<Task> allTasks = taskRepository.findAll();

        List<Task> filteredTask = allTasks.stream()
                .filter(task-> taskStatus == null || task.getStatus().name().equalsIgnoreCase(taskStatus.toString()))
                .toList();

        return filteredTask;
    }

    /*
    todo: Task yerine dto pass.
     */
    public Task updateTask(Long id, Task updatedTask, Long userId){
        Task existingTask = getTaskById(id);

        if(updatedTask.getTitle()!= null){
            existingTask.setTitle(updatedTask.getTitle());
        }

        if (updatedTask.getImage() != null){
            existingTask.setImage(updatedTask.getImage());
        }

        if (updatedTask.getDescription() != null){
            existingTask.setDescription(updatedTask.getDescription());
        }

        if (updatedTask.getDeadLine() != null){
            existingTask.setDeadLine(updatedTask.getDeadLine());
        }

        if (updatedTask.getStatus() != null){
            existingTask.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id){
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    public Task assignedToUser(Long userId, Long taskId){
        Task existingTask = getTaskById(taskId);
        existingTask.setAssignedUserId(userId);
        existingTask.setStatus(TaskStatus.DONE);

        return taskRepository.save(existingTask);
    }

    public List<Task> assignedUsersTask(Long userId, TaskStatus status){
        List<Task> allTasks = taskRepository.findByAssignedUserId(userId);

        List<Task> filteredTasks = allTasks.stream().
                filter(task -> status ==null || task.getStatus().name().equalsIgnoreCase(status.toString()))
                .toList();

        return filteredTasks;

    }

    public Task completeTask(Long taskId){
        Task task = getTaskById(taskId);
        task.setStatus(TaskStatus.DONE);
        return taskRepository.save(task);
    }
}
