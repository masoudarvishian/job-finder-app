package com.zenjob.challenge.controller;

import com.zenjob.challenge.dto.ResponseDto;
import com.zenjob.challenge.service.IJobService;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/shift")
@RequiredArgsConstructor
public class ShiftController {
    private final IJobService jobService;

    @GetMapping(path = "/{jobId}")
    @ResponseBody
    public ResponseDto<GetShiftsResponse> getShifts(@PathVariable("jobId") UUID uuid) {
        List<ShiftResponse> shiftResponses = jobService.getShifts(uuid).stream()
                .map(shift -> ShiftResponse.builder()
                        .id(shift.getId())
                        .talentId(shift.getTalentId())
                        .jobId(shift.getJob().getId())
                        .start(shift.getCreatedAt())
                        .end(shift.getEndTime())
                        .build())
                .collect(Collectors.toList());
        return ResponseDto.<GetShiftsResponse>builder()
                .data(GetShiftsResponse.builder()
                        .shifts(shiftResponses)
                        .build())
                .build();
    }

    @PatchMapping(path = "/{id}/book")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void bookTalent(@PathVariable("id") UUID shiftId, @RequestBody @Valid ShiftController.BookTalentRequestDto dto) {
        jobService.bookTalent(dto.talent, shiftId);
    }

    @DeleteMapping(path = "/{id}/cancel")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelShift(@PathVariable("id") UUID shiftId, @RequestBody @Valid ShiftController.CancelShiftRequestDto dto) {
        jobService.cancelShift(dto.companyId, shiftId);
    }

    @DeleteMapping(path = "/cancel-talent")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void cancelShiftForTalent(@RequestBody @Valid ShiftController.CancelShiftForTalentRequestDto dto) {
        jobService.cancelShiftForTalent(dto.companyId, dto.talentId);
    }

    @NoArgsConstructor
    @Data
    private static class CancelShiftRequestDto {
        UUID companyId;
    }

    @NoArgsConstructor
    @Data
    private static class CancelShiftForTalentRequestDto {
        UUID talentId;
        UUID companyId;
    }

    @NoArgsConstructor
    @Data
    private static class BookTalentRequestDto {
        UUID talent;
    }

    @Builder
    @Data
    private static class GetShiftsResponse {
        List<ShiftResponse> shifts;
    }

    @Builder
    @Data
    private static class ShiftResponse {
        UUID    id;
        UUID    talentId;
        UUID    jobId;
        Instant start;
        Instant end;
    }
}
