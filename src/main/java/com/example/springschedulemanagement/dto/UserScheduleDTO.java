package com.example.springschedulemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserScheduleDTO {

    private Long id;

    @NotNull(message = "유저 정보는 필수 입력 값입니다.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    private String userName;

    @NotNull(message = "일정 정보는 필수 입력 값입니다.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long scheduleId;

    private String scheduleTitle;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
