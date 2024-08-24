package com.example.springschedulemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleUserDTO {
    private Long scheduleId;
    private Long userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
