package org.aldousdev.staffservice.repository;

import org.aldousdev.staffservice.entity.Role;
import org.aldousdev.staffservice.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByDepartmentId(Long departmentId);
    List<Staff> findByRole(Role role);
    List<Staff> findByActive(boolean active);
}
