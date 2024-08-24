package com.example.springschedulemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO extends GenericDTO<Long> {
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long scheduleId;
    private Long userId;
}
