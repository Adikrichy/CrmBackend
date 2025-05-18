package org.aldousdev.staffservice.controller;

import lombok.RequiredArgsConstructor;
import org.aldousdev.staffservice.dto.request.CreateStaffRequest;
import org.aldousdev.staffservice.dto.request.UpdateStaffRequest;
import org.aldousdev.staffservice.dto.response.StaffResponse;
import org.aldousdev.staffservice.entity.Role;
import org.aldousdev.staffservice.entity.Staff;
import org.aldousdev.staffservice.service.StaffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR')")
    public ResponseEntity<StaffResponse> createStaff(
            @RequestBody CreateStaffRequest request,
            @AuthenticationPrincipal Staff currentStaff
    ) {
        return ResponseEntity.ok(staffService.createStaff(request, currentStaff.getRole()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR') or #id == authentication.principal.id")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Long id,
            @RequestBody UpdateStaffRequest request,
            @AuthenticationPrincipal Staff currentStaff
    ) {
        return ResponseEntity.ok(staffService.updateStaff(id, request, currentStaff.getRole()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<Void> deactivateStaff(
            @PathVariable Long id,
            @AuthenticationPrincipal Staff currentStaff
    ) {
        staffService.deactivateStaff(id, currentStaff.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR', 'MANAGER')")
    public ResponseEntity<List<StaffResponse>> getStaffByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(staffService.getStaffByDepartment(departmentId));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('CEO', 'DIRECTOR')")
    public ResponseEntity<List<StaffResponse>> getStaffByRole(@PathVariable Role role) {
        return ResponseEntity.ok(staffService.getStaffByRole(role));
    }
}
