package org.aldousdev.staffservice.controller;

import lombok.RequiredArgsConstructor;
import org.aldousdev.staffservice.dto.request.CreateTaskRequest;
import org.aldousdev.staffservice.dto.response.TaskResponse;
import org.aldousdev.staffservice.entity.Staff;
import org.aldousdev.staffservice.entity.TaskStatus;
import org.aldousdev.staffservice.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER')")
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal Staff currentStaff
    ) {
        return ResponseEntity.ok(taskService.createTask(request, currentStaff));
    }

    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER', 'WORKER')")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus newStatus,
            @AuthenticationPrincipal Staff currentStaff
    ) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, newStatus, currentStaff));
    }

    @GetMapping("/assignee/{assigneeId}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER') or #assigneeId == authentication.principal.id")
    public ResponseEntity<List<TaskResponse>> getTasksByAssignee(@PathVariable Long assigneeId) {
        return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId));
    }

    @GetMapping("/creator/{creatorId}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER')")
    public ResponseEntity<List<TaskResponse>> getTasksByCreator(@PathVariable Long creatorId) {
        return ResponseEntity.ok(taskService.getTasksByCreator(creatorId));
    }
}
