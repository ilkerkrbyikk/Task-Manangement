package com.ilker.submission_service.controller;

import com.ilker.submission_service.clients.TaskClient;
import com.ilker.submission_service.clients.UserClient;
import com.ilker.submission_service.dto.UserDto;
import com.ilker.submission_service.entitiy.Submission;
import com.ilker.submission_service.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final UserClient userClient;
    private final TaskClient taskClient;


    @PostMapping
    public ResponseEntity<Submission> submitTask(@RequestParam Long taskId,
                                                 @RequestParam String githubLink,
                                                 @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Submission submission = submissionService.submitTask(taskId,githubLink,userDto.getId(),jwt);
        return new ResponseEntity<>(submission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Submission submission = submissionService.getTaskSubmissionById(id);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Submission>> getAllSubmissions(
                                                  @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        List<Submission> submission = submissionService.getAllSubmissions();
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Submission>> getSubmissionByTaskId(
            @PathVariable Long taskId,
            @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        List<Submission> submission = submissionService.getTaskSubmissionsByTaskId(taskId);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Submission> acceptOrDecline(
            @PathVariable Long id,
            @RequestParam("status") String status,
            @RequestHeader("Authorization") String jwt){

        UserDto userDto = userClient.getUserProfile(jwt);
        Submission submission = submissionService.acceptDeclineSubmission(id,status);
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

}
