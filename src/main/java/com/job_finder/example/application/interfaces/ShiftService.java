package com.job_finder.example.application.interfaces;

import com.job_finder.example.domain.entity.Shift;
import javassist.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftService {
    List<Shift> getShifts(UUID id);
    void bookTalent(UUID talentId, UUID shiftId) throws NotFoundException;
    void cancelShift(UUID companyId, UUID shiftId) throws NotFoundException;
    Optional<Shift> getShift(UUID id);
    void cancelShiftForTalent(UUID companyId, UUID talentId);
    void clearAllShifts();
}
