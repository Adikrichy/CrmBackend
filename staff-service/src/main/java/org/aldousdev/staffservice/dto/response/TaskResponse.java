package org.aldousdev.staffservice.dto.response;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Staff;
import org.aldousdev.staffservice.entity.Task;
import org.aldousdev.staffservice.entity.TaskPriority;
import org.aldousdev.staffservice.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private TaskPriority priority;
    private Long creatorId;
    private String creatorName;
    private List<Long> assigneeIds;
    private List<String> assigneeNames;

    public static TaskResponse from(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .status(task.getStatus())
                .deadline(task.getDeadline())
                .priority(task.getPriority())
                .creatorId(task.getCreator().getId())
                .creatorName(task.getCreator().getName())
                .assigneeIds(task.getAssignees().stream()
                        .map(Staff::getId)
                        .collect(Collectors.toList()))
                .assigneeNames(task.getAssignees().stream()
                        .map(Staff::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
