package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s JOIN FETCH s.user JOIN FETCH s.comments")
    Page<Schedule> findAll(Pageable pageable);

    @Query("SELECT s FROM Schedule s JOIN FETCH s.user JOIN FETCH s.comments WHERE s.id = :id")
    Optional<Schedule> findById(@Param("id") Long id);

    boolean existsById(Long id);

}