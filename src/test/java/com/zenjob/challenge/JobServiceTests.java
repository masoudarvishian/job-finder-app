package com.zenjob.challenge;

import com.zenjob.challenge.customexception.InvalidActionException;
import com.zenjob.challenge.customexception.InvalidEndDateException;
import com.zenjob.challenge.customexception.InvalidStartDateException;
import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.entity.Shift;
import com.zenjob.challenge.repository.JobRepository;
import com.zenjob.challenge.service.IJobService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class JobServiceTests {

    @Autowired
    private IJobService jobService;

    @Test
    public void job_should_have_at_least_one_shift() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        // when
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);

        // then
        Assertions.assertEquals(1, job.getShifts().size());
    }

    @Test
    public void start_date_cannot_be_in_the_past() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(2);

        // when - then
        Assertions.assertThrows(InvalidStartDateException.class, () ->
                jobService.createJob(UUID.randomUUID(), startDate, endDate));
    }

    @Test
    public void the_end_date_should_be_after_the_start_date() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now();

        // when - then
        Assertions.assertThrows(InvalidEndDateException.class, () ->
                jobService.createJob(UUID.randomUUID(), startDate, endDate));
    }

    @Test
    public void shift_length_should_equal_to_8_hours() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        // when
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);

        // then
        Shift shift = job.getShifts().get(0);
        Duration shiftDuration = Duration.between(shift.getStartTime(), shift.getEndTime());
        Assertions.assertEquals(8, shiftDuration.toHours());
    }

    @Test
    public void job_hours_should_not_be_out_of_scope() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        // when
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);

        // then
        ZonedDateTime zonedStartDateTime = job.getStartTime().atZone(ZoneId.systemDefault());
        ZonedDateTime zonedEndDateTime = job.getEndTime().atZone(ZoneId.systemDefault());
        int startHour = zonedStartDateTime.getHour();
        int endHour = zonedEndDateTime.getHour();
        Assertions.assertTrue(startHour >= 9 && endHour <= 17);
    }

    @Test
    public void cancel_a_job_and_its_shifts_by_company() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);

        // when
        jobService.cancelJob(job.getCompanyId(), job.getId());

        // then
        Assertions.assertFalse(jobService.getJob(job.getId()).isPresent());
        Assertions.assertTrue(jobService.getShifts(job.getId()).isEmpty());
    }

    @Test
    public void a_company_can_only_cancel_its_own_jobs() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);
        UUID companyId = UUID.randomUUID();

        // when - then
        Assertions.assertThrows(InvalidActionException.class, () ->
                jobService.cancelJob(companyId, job.getId()));
    }
}
