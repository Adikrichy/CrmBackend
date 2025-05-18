package org.aldousdev.staffservice.service;

import lombok.RequiredArgsConstructor;
import org.aldousdev.staffservice.dto.request.CreateStaffRequest;
import org.aldousdev.staffservice.dto.request.UpdateStaffRequest;
import org.aldousdev.staffservice.dto.response.StaffResponse;
import org.aldousdev.staffservice.entity.Role;
import org.aldousdev.staffservice.entity.Staff;
import org.aldousdev.staffservice.events.StaffCreatedEvent;
import org.aldousdev.staffservice.events.StaffDeactivatedEvent;
import org.aldousdev.staffservice.exceptions.DepartmentNotFoundException;
import org.aldousdev.staffservice.exceptions.StaffNotFoundException;
import org.aldousdev.staffservice.exceptions.UnauthorizedOperationException;
import org.aldousdev.staffservice.repository.DepartmentRepository;
import org.aldousdev.staffservice.repository.StaffRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;
    private final RabbitTemplate rabbitTemplate;

    public StaffResponse createStaff(CreateStaffRequest request, Role requesterRole) {
        validateStaffCreation(requesterRole);

        Staff staff = Staff.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .hireDate(LocalDate.now())
                .active(true)
                .department(departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new DepartmentNotFoundException(request.getDepartmentId())))
                .build();

        Staff savedStaff = staffRepository.save(staff);

        // Отправка уведомления через RabbitMQ
        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "staff.created",
                StaffCreatedEvent.from(savedStaff)
        );

        return StaffResponse.from(savedStaff);
    }

    public StaffResponse updateStaff(Long id, UpdateStaffRequest request, Role requesterRole) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        validateStaffUpdate(staff, requesterRole);

        if (request.getName() != null) staff.setName(request.getName());
        if (request.getEmail() != null) staff.setEmail(request.getEmail());
        if (request.getPhone() != null) staff.setPhone(request.getPhone());
        if (request.getDepartmentId() != null) {
            staff.setDepartment(departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(request.getDepartmentId())));
        }

        Staff updatedStaff = staffRepository.save(staff);
        return StaffResponse.from(updatedStaff);
    }

    public void deactivateStaff(Long id, Role requesterRole) {
        if (requesterRole != Role.CEO) {
            throw new UnauthorizedOperationException("Only CEO can deactivate staff");
        }

        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new StaffNotFoundException(id));

        staff.setActive(false);
        Staff deactivatedStaff = staffRepository.save(staff);

        // Отправка уведомления через RabbitMQ
        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "staff.deactivated",
                StaffDeactivatedEvent.from(deactivatedStaff)
        );
    }

    public List<StaffResponse> getStaffByDepartment(Long departmentId) {
        return staffRepository.findByDepartmentId(departmentId).stream()
                .map(StaffResponse::from)
                .collect(Collectors.toList());
    }

    public List<StaffResponse> getStaffByRole(Role role) {
        return staffRepository.findByRole(role).stream()
                .map(StaffResponse::from)
                .collect(Collectors.toList());
    }

    private void validateStaffCreation(Role requesterRole) {
        if (requesterRole != Role.CEO && requesterRole != Role.DIRECTOR) {
            throw new UnauthorizedOperationException("Only CEO or Director can create staff");
        }
    }

    private void validateStaffUpdate(Staff staff, Role requesterRole) {
        if (requesterRole != Role.CEO && requesterRole != Role.DIRECTOR) {
            if (requesterRole != staff.getRole()) {
                throw new UnauthorizedOperationException("You can only update your own profile");
            }
        }
    }
}