package resources.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import resources.data.entity.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    Optional<Project> findByIdEquals(long id);

    @Query("update Project p set p.enrolled = p.enrolled + 1 where p.id = :project_id")
    void incrementProjectEnrolled(@Param("project_id") long projectId);
}
