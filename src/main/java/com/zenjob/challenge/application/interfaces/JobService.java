package com.zenjob.challenge.application.interfaces;

import com.zenjob.challenge.domain.entity.Job;
import javassist.NotFoundException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface JobService {
    Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate);
    void cancelJob(UUID companyId, UUID jobId) throws NotFoundException;
    Optional<Job> getJob(UUID id);
}
