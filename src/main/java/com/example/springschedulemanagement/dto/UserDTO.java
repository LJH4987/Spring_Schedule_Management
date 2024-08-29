package com.example.springschedulemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotNull(message = "이름을 입력해주세요")
    @Size(min = 1, max = 10, message = "이름은 1자리 이상 10자리 이하로 설정해주세요")
    private String name;

    @NotNull(message = "이메일을 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "비밀번호를 입력해주세요")
    @Size(min = 7, max = 20, message = "비밀번호는 7자리 이상 20자리 이하로 설정해주세요")
    private String hashedPassword;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}