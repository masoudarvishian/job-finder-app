package com.job_finder.example.rest_api.controller;

import com.job_finder.example.rest_api.dto.job.CancelJobRequestDto;
import com.job_finder.example.rest_api.dto.job.RequestJobRequestDto;
import com.job_finder.example.rest_api.dto.job.RequestJobResponseDto;
import com.job_finder.example.rest_api.dto.ResponseDto;
import com.job_finder.example.domain.entity.Job;
import com.job_finder.example.application.interfaces.JobService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    @ResponseBody
    public ResponseDto<RequestJobResponseDto> requestJob(@RequestBody @Valid RequestJobRequestDto dto) {
        Job job = jobService.createJob(dto.getCompanyId(), dto.getStart(), dto.getEnd());
        return ResponseDto.<RequestJobResponseDto>builder()
                .data(RequestJobResponseDto.builder()
                        .jobId(job.getId())
                        .build())
                .build();
    }

    @DeleteMapping(path = "/{id}/cancel")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelJob(@PathVariable("id") UUID jobId, @RequestBody @Valid CancelJobRequestDto dto) throws NotFoundException {
        jobService.cancelJob(dto.getCompanyId(), jobId);
    }
}
