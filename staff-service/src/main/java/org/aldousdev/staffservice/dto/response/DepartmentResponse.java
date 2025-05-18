package org.aldousdev.staffservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Department;
import org.aldousdev.staffservice.entity.Staff;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private List<Long> staffIds;
    private List<String> staffNames;
    private List<Long> managerIds;
    private List<String> managerNames;

    public static DepartmentResponse from(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .staffIds(department.getStaff().stream()
                        .map(Staff::getId)
                        .collect(Collectors.toList()))
                .staffNames(department.getStaff().stream()
                        .map(Staff::getName)
                        .collect(Collectors.toList()))
                .managerIds(department.getManagers().stream()
                        .map(Staff::getId)
                        .collect(Collectors.toList()))
                .managerNames(department.getManagers().stream()
                        .map(Staff::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
