package com.ilker.submission_service.clients;

import com.ilker.submission_service.dto.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "submission-service", url = "http://localhost:5002/")
public interface TaskClient {

    @GetMapping("/api/tasks/{id}")
   TaskDto getTaskById(@PathVariable Long id,
                                               @RequestHeader("Authorization") String jwt);

    @PutMapping("/api/tasks/{id}/complete")
   TaskDto completeTask(@PathVariable Long id);
}
