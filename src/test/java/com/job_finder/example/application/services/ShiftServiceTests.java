package com.job_finder.example.application.services;

import com.job_finder.example.application.interfaces.JobService;
import com.job_finder.example.application.interfaces.ShiftService;
import com.job_finder.example.domain.entity.Job;
import com.job_finder.example.domain.entity.Shift;
import com.job_finder.example.domain.exceptions.InvalidActionException;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
public class ShiftServiceTests {

    @Autowired
    private JobService jobService;

    @Autowired
    private ShiftService shiftService;

    @AfterEach
    public void cleanUpDatabase() {
        shiftService.clearAllShifts();
        jobService.clearAllJobs();
    }

    @Test
    public void cancel_a_single_shift_by_company() throws NotFoundException {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);
        Shift firstShift = job.getShifts().get(0);

        // when
        shiftService.cancelShift(job.getCompanyId(), firstShift.getId());

        // then
        Assertions.assertFalse(shiftService.getShift(firstShift.getId()).isPresent());
    }

    @Test
    public void a_company_can_only_cancel_its_own_shifts() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);
        Shift firstShift = job.getShifts().get(0);
        UUID companyId = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(() -> shiftService.cancelShift(companyId, firstShift.getId()));

        // then
        assertThat(throwable).isInstanceOf(InvalidActionException.class);
    }

    @Test
    public void cancel_shift_for_a_talent() throws NotFoundException {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);
        Shift firstShift = job.getShifts().get(0);
        Shift secondShift = job.getShifts().get(1);
        UUID talentId = UUID.randomUUID();
        shiftService.bookTalent(talentId, firstShift.getId());
        shiftService.bookTalent(talentId, secondShift.getId());

        // when
        shiftService.cancelShiftForTalent(job.getCompanyId(), talentId);

        // then
        Optional<Shift> firstShiftById = shiftService.getShift(firstShift.getId());
        Optional<Shift> secondShiftById = shiftService.getShift(secondShift.getId());
        Assertions.assertNull(firstShiftById.get().getTalentId());
        Assertions.assertNull(secondShiftById.get().getTalentId());
    }
}
