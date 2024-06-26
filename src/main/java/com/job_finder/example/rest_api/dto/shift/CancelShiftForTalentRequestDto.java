package com.job_finder.example.rest_api.dto.shift;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class CancelShiftForTalentRequestDto {
    private UUID talentId;
    private UUID companyId;
}
