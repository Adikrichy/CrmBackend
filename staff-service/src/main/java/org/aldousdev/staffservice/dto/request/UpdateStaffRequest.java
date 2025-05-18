package org.aldousdev.staffservice.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateStaffRequest {
    private String name;

    @Email
    private String email;

    private String phone;

    private Long departmentId;
}