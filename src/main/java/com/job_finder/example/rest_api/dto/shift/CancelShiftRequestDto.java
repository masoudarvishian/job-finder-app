package com.job_finder.example.rest_api.dto.shift;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class CancelShiftRequestDto {
    private UUID companyId;
}
