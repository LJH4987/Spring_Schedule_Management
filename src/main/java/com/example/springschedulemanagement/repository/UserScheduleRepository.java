package com.example.springschedulemanagement.repository;

import com.example.springschedulemanagement.entity.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {

    boolean existsByUserIdAndScheduleId(Long userId, Long scheduleId);

}
