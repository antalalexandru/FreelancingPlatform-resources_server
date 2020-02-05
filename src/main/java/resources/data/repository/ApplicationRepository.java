package resources.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import resources.data.entity.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    long countByProjectIdEquals(long projectId);
    default List<Application> findByProjectIdEqualsOrderByDateDesc(long projectId, Pageable pageable) {
        return findAll((Specification<Application>) (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("projectId"), String.valueOf(projectId));
        }, pageable).getContent();
    }

    long countAllByProjectIdEqualsAndUserIdEquals(long projectId, long userId);

    Optional<Application> findByIdEquals(long id);
}
