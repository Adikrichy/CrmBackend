package org.aldousdev.staffservice.service;

import lombok.RequiredArgsConstructor;
import org.aldousdev.staffservice.dto.response.DepartmentResponse;
import org.aldousdev.staffservice.entity.Department;
import org.aldousdev.staffservice.entity.Staff;
import org.aldousdev.staffservice.exceptions.DepartmentAlreadyExistsException;
import org.aldousdev.staffservice.exceptions.DepartmentNotFoundException;
import org.aldousdev.staffservice.exceptions.StaffNotFoundException;
import org.aldousdev.staffservice.repository.DepartmentRepository;
import org.aldousdev.staffservice.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final StaffRepository staffRepository;

    public DepartmentResponse createDepartment(String name, List<Long> managerIds) {
        if (departmentRepository.existsByName(name)) {
            throw new DepartmentAlreadyExistsException(name);
        }

        List<Staff> managers = managerIds.stream()
                .map(id -> staffRepository.findById(id)
                        .orElseThrow(() -> new StaffNotFoundException(id)))
                .collect(Collectors.toList());

        Department department = Department.builder()
                .name(name)
                .managers(managers)
                .build();

        Department savedDepartment = departmentRepository.save(department);
        return DepartmentResponse.from(savedDepartment);
    }

    public DepartmentResponse getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
        return DepartmentResponse.from(department);
    }

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentResponse::from)
                .collect(Collectors.toList());
    }

    public DepartmentResponse updateDepartment(Long id, String name, List<Long> managerIds) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        if (name != null && !name.equals(department.getName())) {
            if (departmentRepository.existsByName(name)) {
                throw new DepartmentAlreadyExistsException(name);
            }
            department.setName(name);
        }

        if (managerIds != null) {
            List<Staff> managers = managerIds.stream()
                    .map(managerId -> staffRepository.findById(managerId)
                            .orElseThrow(() -> new StaffNotFoundException(managerId)))
                    .collect(Collectors.toList());
            department.setManagers(managers);
        }

        Department updatedDepartment = departmentRepository.save(department);
        return DepartmentResponse.from(updatedDepartment);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        // Проверяем, есть ли сотрудники в отделе
        if (!department.getStaff().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with active staff members");
        }

        departmentRepository.delete(department);
    }

    public List<DepartmentResponse> getDepartmentsByManager(Long managerId) {
        Staff manager = staffRepository.findById(managerId)
                .orElseThrow(() -> new StaffNotFoundException(managerId));

        return departmentRepository.findAll().stream()
                .filter(department -> department.getManagers().contains(manager))
                .map(DepartmentResponse::from)
                .collect(Collectors.toList());
    }
}