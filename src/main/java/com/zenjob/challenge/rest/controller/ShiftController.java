package com.zenjob.challenge.rest.controller;

import com.zenjob.challenge.rest.dto.ResponseDto;
import com.zenjob.challenge.application.interfaces.JobService;
import com.zenjob.challenge.rest.dto.shift.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/shift")
@RequiredArgsConstructor
public class ShiftController {
    private final JobService jobService;

    @GetMapping(path = "/{jobId}")
    @ResponseBody
    public ResponseDto<GetShiftsResponseDto> getShifts(@PathVariable("jobId") UUID uuid) {
        List<ShiftResponseDto> shiftResponses = getShiftResponses(uuid);
        return ResponseDto.<GetShiftsResponseDto>builder()
                .data(GetShiftsResponseDto.builder()
                        .shifts(shiftResponses)
                        .build())
                .build();
    }

    @PatchMapping(path = "/{id}/book")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void bookTalent(@PathVariable("id") UUID shiftId, @RequestBody @Valid BookTalentRequestDto dto) {
        jobService.bookTalent(dto.getTalentId(), shiftId);
    }

    @PatchMapping(path = "/cancel-talent")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelShiftForTalent(@RequestBody @Valid CancelShiftForTalentRequestDto dto) {
        jobService.cancelShiftForTalent(dto.getCompanyId(), dto.getTalentId());
    }

    @DeleteMapping(path = "/{id}/cancel")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelShift(@PathVariable("id") UUID shiftId, @RequestBody @Valid CancelShiftRequestDto dto) {
        jobService.cancelShift(dto.getCompanyId(), shiftId);
    }

    private List<ShiftResponseDto> getShiftResponses(UUID uuid) {
        return jobService.getShifts(uuid).stream()
                .map(shift -> ShiftResponseDto.builder()
                        .id(shift.getId())
                        .talentId(shift.getTalentId())
                        .jobId(shift.getJob().getId())
                        .start(shift.getCreatedAt())
                        .end(shift.getEndTime())
                        .build())
                .collect(Collectors.toList());
    }
}
