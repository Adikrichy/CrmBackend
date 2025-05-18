package org.aldousdev.staffservice.events;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Staff;
import org.aldousdev.staffservice.entity.Task;


@Data
@Builder
public class TaskAssignedEvent {
    private Long taskId;
    private String description;
    private Long assigneeId;
    private String assigneeName;
    private String assigneeEmail;

    public static TaskAssignedEvent from(Task task, Staff assignee) {
        return TaskAssignedEvent.builder()
                .taskId(task.getId())
                .description(task.getDescription())
                .assigneeId(assignee.getId())
                .assigneeName(assignee.getName())
                .assigneeEmail(assignee.getEmail())
                .build();
    }
}
