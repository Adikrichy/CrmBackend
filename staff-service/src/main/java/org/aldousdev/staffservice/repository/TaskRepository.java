package org.aldousdev.staffservice.repository;
import org.aldousdev.staffservice.entity.Task;
import org.aldousdev.staffservice.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatorId(Long creatorId);
    List<Task> findByAssigneesId(Long assigneeId);
    List<Task> findByStatus(TaskStatus status);
}
