package com.example.springschedulemanagement.dto;

import com.example.springschedulemanagement.dto.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {

    private Long id;

    @NotNull(message = "부여할 권한을 입력해주세요")
    @Pattern(regexp = "^(ADMIN|USER)$", message = "권한은 ADMIN 또는 USER만 가능합니다.")
    private RoleName roleName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    private String userName;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

}
