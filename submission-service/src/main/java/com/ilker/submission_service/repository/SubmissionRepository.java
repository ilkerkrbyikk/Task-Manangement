package com.ilker.submission_service.repository;

import com.ilker.submission_service.entitiy.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission,Long> {

    List<Submission> findByTaskId(Long taskId);
}
