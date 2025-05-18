package org.aldousdev.staffservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.aldousdev.staffservice.entity.TaskPriority;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateTaskRequest {
    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private List<Long> assigneeIds;
}