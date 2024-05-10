package com.zenjob.challenge.service;

import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.entity.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IJobService {
    Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate);
    List<Shift> getShifts(UUID id);
    void bookTalent(UUID talent, UUID shiftId);
    void cancelJob(UUID companyId, UUID jobId);
    Optional<Job> getJob(UUID id);
}
