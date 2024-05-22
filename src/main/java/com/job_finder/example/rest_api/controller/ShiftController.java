package com.job_finder.example.rest_api.controller;

import com.job_finder.example.application.interfaces.ShiftService;
import com.job_finder.example.rest_api.dto.ResponseDto;
import com.job_finder.example.rest_api.dto.shift.*;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/shift")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

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
    public void bookTalent(@PathVariable("id") UUID shiftId, @RequestBody @Valid BookTalentRequestDto dto) throws NotFoundException {
        shiftService.bookTalent(dto.getTalentId(), shiftId);
    }

    @PatchMapping(path = "/cancel-talent")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelShiftForTalent(@RequestBody @Valid CancelShiftForTalentRequestDto dto) {
        shiftService.cancelShiftForTalent(dto.getCompanyId(), dto.getTalentId());
    }

    @DeleteMapping(path = "/{id}/cancel")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelShift(@PathVariable("id") UUID shiftId, @RequestBody @Valid CancelShiftRequestDto dto) throws NotFoundException {
        shiftService.cancelShift(dto.getCompanyId(), shiftId);
    }

    private List<ShiftResponseDto> getShiftResponses(UUID uuid) {
        return shiftService.getShifts(uuid).stream()
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
