package com.zenjob.challenge;

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
    public void start_date_cannot_be_in_the_past() {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(2);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            jobService.createJob(UUID.randomUUID(), startDate, endDate);
        });
    }

}
