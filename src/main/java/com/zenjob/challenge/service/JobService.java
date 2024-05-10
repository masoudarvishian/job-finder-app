package com.zenjob.challenge.service;

import com.zenjob.challenge.customexception.InvalidActionException;
import com.zenjob.challenge.customexception.InvalidEndDateException;
import com.zenjob.challenge.customexception.InvalidStartDateException;
import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.entity.Shift;
import com.zenjob.challenge.repository.JobRepository;
import com.zenjob.challenge.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RequiredArgsConstructor
@Service
@Transactional
public class JobService implements IJobService {
    private final JobRepository   jobRepository;
    private final ShiftRepository shiftRepository;

    public Job createJob(UUID companyId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now()))
            throw new InvalidStartDateException();

        if (endDate.isBefore(startDate))
            throw new InvalidEndDateException();

        Job job = Job.builder()
                .id(UUID.randomUUID())
                .companyId(companyId)
                .startTime(startDate.atTime(9, 0, 0).toInstant(ZoneOffset.UTC))
                .endTime(endDate.atTime(17, 0, 0).toInstant(ZoneOffset.UTC))
                .build();
        job.setShifts(LongStream.range(0, ChronoUnit.DAYS.between(startDate, endDate))
                .mapToObj(idx -> startDate.plus(idx, ChronoUnit.DAYS))
                .map(date -> Shift.builder()
                        .id(UUID.randomUUID())
                        .job(job)
                        .startTime(date.atTime(9, 0, 0).toInstant(ZoneOffset.UTC))
                        .endTime(date.atTime(17, 0, 0).toInstant(ZoneOffset.UTC))
                        .build())
                .collect(Collectors.toList()));
        return jobRepository.save(job);
    }

    public List<Shift> getShifts(UUID id) {
        return shiftRepository.findAllByJobId(id);
    }

    public void bookTalent(UUID talentId, UUID shiftId) {
//        Optional<Shift> shiftById = shiftRepository.findById(shiftId);
//        shiftById.map(shift -> shiftRepository.save(shift.setTalentId(talentId)));

        Optional<Shift> shiftById = shiftRepository.findById(shiftId);
        shiftById.get().setTalentId(talentId);
        shiftRepository.save(shiftById.get());
    }

    @Override
    public void cancelJob(UUID companyId, UUID jobId) {
        Optional<Job> job = getJob(jobId);
        if (!job.get().getCompanyId().equals(companyId))
            throw new InvalidActionException();

        jobRepository.deleteById(jobId);
    }

    @Override
    public Optional<Job> getJob(UUID id) {
        return jobRepository.findById(id);
    }

    @Override
    public void cancelShift(UUID companyId, UUID shiftId) {
        Optional<Shift> shift = getShift(shiftId);
        if (!shift.get().getJob().getCompanyId().equals(companyId))
            throw new InvalidActionException();

        shiftRepository.deleteById(shiftId);
    }

    @Override
    public Optional<Shift> getShift(UUID id) {
        return shiftRepository.findById(id);
    }

    @Override
    public void cancelShiftForTalent(UUID companyId, UUID talentId) {
        List<Shift> shifts = getShiftsByTalentIdAndCompanyId(talentId, companyId);
        shifts.forEach(shift -> {
            shift.setTalentId(null);
            shiftRepository.save(shift);
        });
    }

    private List<Shift> getShiftsByTalentIdAndCompanyId(UUID talentId, UUID companyId) {
        List<Shift> shifts = shiftRepository.findAllByTalentId(talentId);
        return shifts.stream()
                .filter(shift -> companyId.equals(shift.getJob().getCompanyId()))
                .collect(Collectors.toList());
    }
}
