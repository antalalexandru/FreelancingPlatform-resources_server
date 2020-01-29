package resources.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resources.data.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
