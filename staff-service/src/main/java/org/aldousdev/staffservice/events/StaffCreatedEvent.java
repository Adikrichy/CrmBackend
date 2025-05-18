package org.aldousdev.staffservice.events;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Staff;

@Data
@Builder
public class StaffCreatedEvent {
    private Long staffId;
    private String name;
    private String email;
    private String departmentName;

    public static StaffCreatedEvent from(Staff staff) {
        return StaffCreatedEvent.builder()
                .staffId(staff.getId())
                .name(staff.getName())
                .email(staff.getEmail())
                .departmentName(staff.getDepartment().getName())
                .build();
    }
}

