package com.zenjob.challenge.application.services;

import com.zenjob.challenge.application.interfaces.JobService;
import com.zenjob.challenge.domain.exceptions.InvalidActionException;
import com.zenjob.challenge.domain.exceptions.InvalidEndDateException;
import com.zenjob.challenge.domain.exceptions.InvalidStartDateException;
import com.zenjob.challenge.domain.entity.Job;
import com.zenjob.challenge.domain.entity.Shift;
import com.zenjob.challenge.repository.JobRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RequiredArgsConstructor
@Service
@Transactional
class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;

    private static final int START_HOUR = 9;
    private static final int END_HOUR = 17;

    public Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
        Job job = buildJob(companyId, startDate, endDate);
        setJobShifts(job, startDate, endDate);
        return jobRepository.save(job);
    }

    @Override
    public void cancelJob(UUID companyId, UUID jobId) throws NotFoundException {
        Optional<Job> job = getJob(jobId);
        if (!job.isPresent())
            throw new NotFoundException("Job not found!");
        if (!job.get().getCompanyId().equals(companyId))
            throw new InvalidActionException("You cannot cancel job of other companies");

        jobRepository.deleteById(jobId);
    }

    @Override
    public Optional<Job> getJob(UUID id) {
        return jobRepository.findById(id);
    }

    private static Job buildJob(UUID companyId, LocalDate startDate, LocalDate endDate) {
        return Job.builder()
                .id(UUID.randomUUID())
                .companyId(companyId)
                .startTime(startDate.atTime(START_HOUR, 0, 0).toInstant(ZoneOffset.UTC))
                .endTime(endDate.atTime(END_HOUR, 0, 0).toInstant(ZoneOffset.UTC))
                .build();
    }

    private static void setJobShifts(Job job, LocalDate startDate, LocalDate endDate) {
        job.setShifts(LongStream.range(0, ChronoUnit.DAYS.between(startDate, endDate))
                .mapToObj(idx -> startDate.plus(idx, ChronoUnit.DAYS))
                .map(date -> Shift.builder()
                        .id(UUID.randomUUID())
                        .job(job)
                        .startTime(date.atTime(START_HOUR, 0, 0).toInstant(ZoneOffset.UTC))
                        .endTime(date.atTime(END_HOUR, 0, 0).toInstant(ZoneOffset.UTC))
                        .build())
                .collect(Collectors.toList()));
    }

    private static void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isEqual(endDate))
            throw new IllegalArgumentException("Start date and end date should not be equal");

        if (startDate.isBefore(LocalDate.now()))
            throw new InvalidStartDateException("Start date cannot be before now");

        if (endDate.isBefore(startDate))
            throw new InvalidEndDateException("End date cannot be before start date");
    }
}
