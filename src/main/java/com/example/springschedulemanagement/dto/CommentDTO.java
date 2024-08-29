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
public class CommentDTO {

    private Long id;

    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long scheduleId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String scheduleTitle;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    private String userName;

}

