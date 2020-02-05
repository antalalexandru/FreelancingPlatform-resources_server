package resources.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagsStatisticsDTO {
    private String name;
    private long count;
}
