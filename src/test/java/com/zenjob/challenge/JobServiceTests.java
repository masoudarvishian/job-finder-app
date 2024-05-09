package com.zenjob.challenge;

import com.zenjob.challenge.entity.Job;
import com.zenjob.challenge.service.IJobService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class JobServiceTests {

    @Autowired
    private IJobService jobService;

    @Test
    public void job_should_have_at_least_one_shift() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        Job job = jobService.createJob(UUID.randomUUID(), startDate, endDate);

        Assertions.assertEquals(1, job.getShifts().size());
    }

    @Test
    public void start_date_cannot_be_in_the_past() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(2);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                jobService.createJob(UUID.randomUUID(), startDate, endDate));
    }

}
