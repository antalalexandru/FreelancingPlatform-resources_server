package resources.data.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import resources.data.dto.TagsStatisticsDTO;
import resources.data.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(nativeQuery = true, value = "SELECT tags.name AS name, COUNT(*) AS count " +
            "FROM tags " +
            "INNER JOIN projects_tags ON tags.tag_id = projects_tags.tag_id " +
            "GROUP BY tags.name " +
            "ORDER BY COUNT(*) DESC")
    List<Object[]> tagStatsUnprocessed();

    default List<TagsStatisticsDTO> getTagsStatistics() {
        return tagStatsUnprocessed().stream()
                .map(data -> TagsStatisticsDTO.builder()
                        .name(data[0].toString())
                        .count(Integer.parseInt(data[1].toString()))
                        .build())
                .collect(Collectors.toList());
    }
}
