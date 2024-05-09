package com.zenjob.challenge.service;

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
                .startTime(startDate.atTime(8, 0, 0).toInstant(ZoneOffset.UTC))
                .endTime(endDate.atTime(17, 0, 0).toInstant(ZoneOffset.UTC))
                .build();
        job.setShifts(LongStream.range(0, ChronoUnit.DAYS.between(startDate, endDate))
                .mapToObj(idx -> startDate.plus(idx, ChronoUnit.DAYS))
                .map(date -> Shift.builder()
                        .id(UUID.randomUUID())
                        .job(job)
                        .startTime(date.atTime(8, 0, 0).toInstant(ZoneOffset.UTC))
                        .endTime(date.atTime(17, 0, 0).toInstant(ZoneOffset.UTC))
                        .build())
                .collect(Collectors.toList()));
        return jobRepository.save(job);
    }

    public List<Shift> getShifts(UUID id) {
        return shiftRepository.findAllByJobId(id);
    }

    public void bookTalent(UUID talent, UUID shiftId) {
        Optional<Shift> shiftById = shiftRepository.findById(shiftId);
        shiftById.map(shift -> shiftRepository.save(shift.setTalentId(talent)));
    }
}
