package org.aldousdev.staffservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.aldousdev.staffservice.entity.Role;

@Data
public class CreateStaffRequest {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @NotNull
    private Role role;

    @NotNull
    private Long departmentId;
}