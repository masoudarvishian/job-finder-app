package com.zenjob.challenge.rest_api.dto.shift;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Builder
@Data
public class ShiftResponseDto {
    private UUID id;
    private UUID talentId;
    private UUID jobId;
    private Instant start;
    private Instant end;
}
