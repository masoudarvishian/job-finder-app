package com.job_finder.example.rest_api.dto.job;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class CancelJobRequestDto {
    private UUID companyId;
}
