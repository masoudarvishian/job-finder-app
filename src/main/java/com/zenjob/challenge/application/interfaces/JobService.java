package com.zenjob.challenge.application.interfaces;

import com.zenjob.challenge.domain.entity.Job;
import com.zenjob.challenge.domain.entity.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobService {
    Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate);
    List<Shift> getShifts(UUID id);
    void bookTalent(UUID talent, UUID shiftId);
    void cancelJob(UUID companyId, UUID jobId);
    Optional<Job> getJob(UUID id);
    void cancelShift(UUID companyId, UUID shiftId);
    Optional<Shift> getShift(UUID id);
    void cancelShiftForTalent(UUID companyId, UUID talentId);
}
