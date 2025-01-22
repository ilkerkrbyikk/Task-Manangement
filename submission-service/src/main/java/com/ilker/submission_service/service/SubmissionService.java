package com.ilker.submission_service.service;

import com.ilker.submission_service.clients.TaskClient;
import com.ilker.submission_service.clients.UserClient;
import com.ilker.submission_service.dto.TaskDto;
import com.ilker.submission_service.entitiy.Submission;
import com.ilker.submission_service.exception.TaskNotFoundException;
import com.ilker.submission_service.exception.TaskSubNotFoundException;
import com.ilker.submission_service.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final TaskClient taskClient;
    private final UserClient userClient;

    public Submission submitTask(Long taskId, String githubLink, Long userId,String jwt){
        TaskDto task =taskClient.getTaskById(taskId,jwt);

        if(task != null){
            Submission submission = new Submission();
            submission.setTaskId(taskId);
            submission.setUserId(userId);
            submission.setGithubLink(githubLink);
            submission.setSubmissionTime(LocalDateTime.now());

            return submissionRepository.save(submission);
        }
        throw new TaskNotFoundException("Task not found with given ID: " + taskId);
    }

    public Submission getTaskSubmissionById(Long submissionId){
        return submissionRepository.findById(submissionId)
                .orElseThrow(()-> new TaskSubNotFoundException("Task submission not found with given ID: " + submissionId));
    }

    public List<Submission> getAllSubmissions(){
        return submissionRepository.findAll();
    }
    
    public List<Submission> getTaskSubmissionsByTaskId(Long taskId){
        return submissionRepository.findByTaskId(taskId);
    }

    public Submission acceptDeclineSubmission(Long id, String status){
        Submission submission = getTaskSubmissionById(id);
        submission.setStatus(status);

        if (status.equals("ACCEPT")){
            taskClient.completeTask(submission.getTaskId());
        }

        return submissionRepository.save(submission);
    }
}
