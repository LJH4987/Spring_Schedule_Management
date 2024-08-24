package com.example.springschedulemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO extends GenericDTO<Long> {
    private String name;
    private String email;
    private String hashedPassword;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
