package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findById(Long id);

    Page<Schedule> findAll(Pageable pageable);

    boolean existsById(Long id);
}