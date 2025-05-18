package org.aldousdev.staffservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Role;
import org.aldousdev.staffservice.entity.Staff;

import java.time.LocalDate;

@Data
@Builder
public class StaffResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDate hireDate;
    private boolean active;
    private Long departmentId;
    private String departmentName;

    public static StaffResponse from(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .phone(staff.getPhone())
                .role(staff.getRole())
                .hireDate(staff.getHireDate())
                .active(staff.isActive())
                .departmentId(staff.getDepartment().getId())
                .departmentName(staff.getDepartment().getName())
                .build();
    }
}