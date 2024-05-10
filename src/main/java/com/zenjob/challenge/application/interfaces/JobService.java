package com.zenjob.challenge.application.interfaces;

import com.zenjob.challenge.domain.entity.Job;
import com.zenjob.challenge.domain.entity.Shift;
import javassist.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobService {
    Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate);
    List<Shift> getShifts(UUID id);
    void bookTalent(UUID talent, UUID shiftId) throws NotFoundException;
    void cancelJob(UUID companyId, UUID jobId) throws NotFoundException;
    Optional<Job> getJob(UUID id);
    void cancelShift(UUID companyId, UUID shiftId) throws NotFoundException;
    Optional<Shift> getShift(UUID id);
    void cancelShiftForTalent(UUID companyId, UUID talentId);
}
