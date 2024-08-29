package com.example.springschedulemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {

    private Long id;

    @NotBlank(message = "일정 제목을 입력해주세요")
    private String title;

    @NotBlank(message = "일정 내용을 입력해주세요")
    private String description;

    private int commentCount;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    private String userName;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String date;

    private String weather;

}