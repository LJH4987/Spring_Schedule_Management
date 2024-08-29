package com.example.springschedulemanagement.service;

import com.example.springschedulemanagement.dto.UserScheduleDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserScheduleService {
    UserScheduleDTO assignUserToSchedule(Long userId, Long scheduleId);
    UserScheduleDTO updateUserSchedule(Long id, UserScheduleDTO userScheduleDTO);
    void deleteUserSchedule(Long id);
    Optional<UserScheduleDTO> getUserScheduleById(Long id);
    List<UserScheduleDTO> getAllUserSchedules();
}
