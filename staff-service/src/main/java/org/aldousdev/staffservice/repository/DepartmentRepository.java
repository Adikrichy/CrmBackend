package org.aldousdev.staffservice.repository;
import org.aldousdev.staffservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);

    @Query("SELECT d FROM Department d JOIN d.managers m WHERE m.id = :managerId")
    List<Department> findByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT d FROM Department d WHERE SIZE(d.staff) > 0")
    List<Department> findDepartmentsWithStaff();
}
