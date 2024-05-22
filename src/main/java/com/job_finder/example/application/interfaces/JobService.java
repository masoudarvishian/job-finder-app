package com.job_finder.example.application.interfaces;

import com.job_finder.example.domain.entity.Job;
import javassist.NotFoundException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface JobService {
    Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate);
    void cancelJob(UUID companyId, UUID jobId) throws NotFoundException;
    Optional<Job> getJob(UUID id);
    void clearAllJobs();
}
