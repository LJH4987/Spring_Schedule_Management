package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.ScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ScheduleService {
    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);
    ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO);
    void deleteSchedule(Long id);
    Optional<ScheduleDTO> getScheduleById(Long id);
    Page<ScheduleDTO> getAllSchedules(Pageable pageable);
}