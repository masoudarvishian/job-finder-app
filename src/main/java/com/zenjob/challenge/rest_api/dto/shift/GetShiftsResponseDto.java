package com.zenjob.challenge.rest_api.dto.shift;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetShiftsResponseDto {
    private List<ShiftResponseDto> shifts;
}
