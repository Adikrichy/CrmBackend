package org.aldousdev.staffservice.events;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Role;
import org.aldousdev.staffservice.entity.Staff;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
public class StaffDeactivatedEvent {
    private Long staffId;
    private String name;
    private String email;
    private Role role;
    private String departmentName;
    private Long departmentId;
    private List<StaffManager> managers;

    @Data
    @Builder
    public static class StaffManager {
        private Long id;
        private String name;
        private String email;
        private Role role;
    }

    public static StaffDeactivatedEvent from(Staff staff) {
        return StaffDeactivatedEvent.builder()
                .staffId(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .role(staff.getRole())
                .departmentName(staff.getDepartment().getName())
                .departmentId(staff.getDepartment().getId())
                .managers(staff.getDepartment().getManagers().stream()
                        .map(manager -> StaffManager.builder()
                                .id(manager.getId())
                                .name(manager.getName())
                                .email(manager.getEmail())
                                .role(manager.getRole())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
