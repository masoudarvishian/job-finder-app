package com.job_finder.example.repository;

import com.job_finder.example.domain.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findAllByJobId(UUID jobId);
    List<Shift> findAllByTalentId(UUID talentId);
}
