package org.aldousdev.staffservice.controller;

import lombok.RequiredArgsConstructor;
import org.aldousdev.staffservice.dto.response.DepartmentResponse;
import org.aldousdev.staffservice.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<DepartmentResponse> createDepartment(
            @RequestParam String name,
            @RequestParam List<Long> managerIds
    ) {
        return ResponseEntity.ok(departmentService.createDepartment(name, managerIds));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER')")
    public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER')")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<Long> managerIds
    ) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, name, managerIds));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER')")
    public ResponseEntity<List<DepartmentResponse>> getDepartmentsByManager(
            @PathVariable Long managerId
    ) {
        return ResponseEntity.ok(departmentService.getDepartmentsByManager(managerId));
    }
}