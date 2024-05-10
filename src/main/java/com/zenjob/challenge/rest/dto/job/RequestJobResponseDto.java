package com.zenjob.challenge.rest.dto.job;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RequestJobResponseDto {
    private UUID jobId;
}
