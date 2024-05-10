package com.zenjob.challenge.rest.dto.job;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
public class CancelJobRequestDto {
    private UUID companyId;
}
