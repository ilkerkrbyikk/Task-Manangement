package com.ilker.task_service.controller;

import com.ilker.task_service.client.UserClient;
import com.ilker.task_service.dto.UserDto;
import com.ilker.task_service.entitiy.Task;
import com.ilker.task_service.enums.TaskStatus;
import com.ilker.task_service.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserClient userClient;

    //todo: controllerdaki işlemleri service çek.
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task,
                                           @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Task createdTask = taskService.createTask(task,userDto.getRole());

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id,
                                            @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Task task = taskService.getTaskById(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> getAssignedUsersTask(@RequestParam(required = false)TaskStatus status,
                                                          @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        List<Task> tasks = taskService.assignedUsersTask(userDto.getId(),status);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false)TaskStatus status,
                                                           @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        List<Task> tasks = taskService.getAllTask(status);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{id}/user/{userId}/assigned")
    public ResponseEntity<Task> assignedTaskToUser(@PathVariable Long id,
                                                  @PathVariable Long userId,
                                                  @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Task task = taskService.assignedToUser(userId,id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                                                   @RequestBody Task req,
                                                   @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Task task = taskService.updateTask(id,req, userDto.getId());

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id){

        Task task = taskService.completeTask(id);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){

        taskService.deleteTask(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
