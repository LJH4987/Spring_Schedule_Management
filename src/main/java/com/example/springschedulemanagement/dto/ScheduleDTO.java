package com.example.springschedulemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleDTO extends GenericDTO<Long> {
    private String title;
    private String description;
    private LocalDateTime date;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private List<UserDTO> users;
    private Double temperature;
    private String weatherDescription;
}
