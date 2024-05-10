package com.zenjob.challenge.application.services;

import com.zenjob.challenge.application.interfaces.ShiftService;
import com.zenjob.challenge.domain.entity.Shift;
import com.zenjob.challenge.domain.exceptions.InvalidActionException;
import com.zenjob.challenge.repository.ShiftRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    @Override
    public List<Shift> getShifts(UUID id) {
        return shiftRepository.findAllByJobId(id);
    }

    @Override
    public void bookTalent(UUID talentId, UUID shiftId) throws NotFoundException {
        Optional<Shift> shiftById = shiftRepository.findById(shiftId);
        if (!shiftById.isPresent())
            throw new NotFoundException("Shift not found!");
        shiftById.get().setTalentId(talentId);
        shiftRepository.save(shiftById.get());
    }

    @Override
    public void cancelShift(UUID companyId, UUID shiftId) throws NotFoundException {
        Optional<Shift> shift = getShift(shiftId);
        if (!shift.isPresent())
            throw new NotFoundException("Shift not found!");
        if (!shift.get().getJob().getCompanyId().equals(companyId))
            throw new InvalidActionException("You cannot cancel shift of other companies");

        shiftRepository.deleteById(shiftId);
    }

    @Override
    public Optional<Shift> getShift(UUID id) {
        return shiftRepository.findById(id);
    }

    @Override
    public void cancelShiftForTalent(UUID companyId, UUID talentId) {
        List<Shift> shifts = getShiftsByTalentIdAndCompanyId(talentId, companyId);
        shifts.forEach(shift -> {
            shift.setTalentId(null);
            shiftRepository.save(shift);
        });
    }

    @Override
    public void clearAllShifts() {
        shiftRepository.deleteAll();
    }

    private List<Shift> getShiftsByTalentIdAndCompanyId(UUID talentId, UUID companyId) {
        List<Shift> shifts = shiftRepository.findAllByTalentId(talentId);
        return shifts.stream()
                .filter(shift -> companyId.equals(shift.getJob().getCompanyId()))
                .collect(Collectors.toList());
    }
}
