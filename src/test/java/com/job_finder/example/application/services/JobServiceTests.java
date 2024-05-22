package com.job_finder.example.application.services;

import com.job_finder.example.domain.entity.Job;
import com.job_finder.example.domain.entity.Shift;
import com.job_finder.example.application.interfaces.ShiftService;
import com.job_finder.example.domain.exceptions.InvalidActionException;
import com.job_finder.example.domain.exceptions.InvalidEndDateException;
import com.job_finder.example.domain.exceptions.InvalidStartDateException;
import com.job_finder.example.application.interfaces.JobService;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@SpringBootTest
public class JobServiceTests {

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

        // When
        Throwable throwable = catchThrowable(() ->
            jobService.createJob(UUID.randomUUID(), startDate, endDate)
        );

        // Then
        assertThat(throwable).isInstanceOf(InvalidStartDateException.class);
    }

    @Test
    public void start_date_should_not_be_equal_to_end_date() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        // when
        Throwable throwable = catchThrowable(() -> jobService.createJob(UUID.randomUUID(), startDate, endDate));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void the_end_date_should_be_after_the_start_date() {
        // given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now();

        // when
        Throwable throwable = catchThrowable(() -> jobService.createJob(UUID.randomUUID(), startDate, endDate));

        // then
        assertThat(throwable).isInstanceOf(InvalidEndDateException.class);
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
    public void cancel_a_job_and_its_shifts_by_company() throws NotFoundException {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);

        // when
        jobService.cancelJob(job.getCompanyId(), job.getId());

        // then
        Assertions.assertFalse(jobService.getJob(job.getId()).isPresent());
        Assertions.assertTrue(shiftService.getShifts(job.getId()).isEmpty());
    }

    @Test
    public void a_company_can_only_cancel_its_own_jobs() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);
        UUID companyId = UUID.randomUUID();

        // when
        Throwable throwable = catchThrowable(() -> jobService.cancelJob(companyId, job.getId()));

        // then
        assertThat(throwable).isInstanceOf(InvalidActionException.class);
    }
}
