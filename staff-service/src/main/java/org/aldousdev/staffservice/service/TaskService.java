package org.aldousdev.staffservice.service;

import lombok.RequiredArgsConstructor;
import org.aldousdev.staffservice.dto.request.CreateTaskRequest;
import org.aldousdev.staffservice.dto.response.TaskResponse;
import org.aldousdev.staffservice.events.TaskAssignedEvent;
import org.aldousdev.staffservice.events.TaskStatusUpdatedEvent;
import org.aldousdev.staffservice.exceptions.StaffNotFoundException;
import org.aldousdev.staffservice.exceptions.TaskNotFoundException;
import org.aldousdev.staffservice.exceptions.UnauthorizedOperationException;
import org.aldousdev.staffservice.entity.*;
import org.aldousdev.staffservice.repository.StaffRepository;
import org.aldousdev.staffservice.repository.TaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final StaffRepository staffRepository;
    private final RabbitTemplate rabbitTemplate;

    public TaskResponse createTask(CreateTaskRequest request, Staff creator) {
        validateTaskCreation(creator);

        Task task = Task.builder()
                .description(request.getDescription())
                .status(TaskStatus.TODO)
                .deadline(request.getDeadline())
                .priority(request.getPriority())
                .creator(creator)
                .assignees(request.getAssigneeIds().stream()
                        .map(id -> staffRepository.findById(id)
                                .orElseThrow(() -> new StaffNotFoundException(id)))
                        .collect(Collectors.toList()))
                .build();

        Task savedTask = taskRepository.save(task);

        // Отправка уведомлений через RabbitMQ
        savedTask.getAssignees().forEach(assignee ->
                rabbitTemplate.convertAndSend(
                        "notification.exchange",
                        "task.assigned",
                        TaskAssignedEvent.from(savedTask, assignee)
                )
        );

        return TaskResponse.from(savedTask);
    }

    public TaskResponse updateTaskStatus(Long taskId, TaskStatus newStatus, Staff staff) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        validateTaskStatusUpdate(task, newStatus, staff);

        TaskStatus oldStatus = task.getStatus();
        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);

        // Отправка уведомления через RabbitMQ
        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "task.status.updated",
                TaskStatusUpdatedEvent.from(updatedTask, oldStatus)
        );

        return TaskResponse.from(updatedTask);
    }

    public List<TaskResponse> getTasksByAssignee(Long assigneeId) {
        return taskRepository.findByAssigneesId(assigneeId).stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByCreator(Long creatorId) {
        return taskRepository.findByCreatorId(creatorId).stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
    }

    private void validateTaskCreation(Staff creator) {
        if (creator.getRole() == Role.WORKER) {
            throw new UnauthorizedOperationException("Workers cannot create tasks");
        }
    }

    private void validateTaskStatusUpdate(Task task, TaskStatus newStatus, Staff staff) {
        if (staff.getRole() == Role.WORKER && !task.getAssignees().contains(staff)) {
            throw new UnauthorizedOperationException("You can only update status of tasks assigned to you");
        }
    }
}