package org.aldousdev.staffservice.events;

import lombok.Builder;
import lombok.Data;
import org.aldousdev.staffservice.entity.Task;
import org.aldousdev.staffservice.entity.TaskPriority;
import org.aldousdev.staffservice.entity.TaskStatus;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
public class TaskStatusUpdatedEvent {
    private Long taskId;
    private String description;
    private TaskStatus oldStatus;
    private TaskStatus newStatus;
    private TaskPriority priority;
    private Long creatorId;
    private String creatorName;
    private String creatorEmail;
    private List<TaskAssignee> assignees;

    @Data
    @Builder
    public static class TaskAssignee {
        private Long id;
        private String name;
        private String email;
    }

    public static TaskStatusUpdatedEvent from(Task task, TaskStatus oldStatus) {
        return TaskStatusUpdatedEvent.builder()
                .taskId(task.getId())
                .description(task.getDescription())
                .oldStatus(oldStatus)
                .newStatus(task.getStatus())
                .priority(task.getPriority())
                .creatorId(task.getCreator().getId())
                .creatorName(task.getCreator().getName())
                .creatorEmail(task.getCreator().getEmail())
                .assignees(task.getAssignees().stream()
                        .map(assignee -> TaskAssignee.builder()
                                .id(assignee.getId())
                                .name(assignee.getName())
                                .email(assignee.getEmail())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
