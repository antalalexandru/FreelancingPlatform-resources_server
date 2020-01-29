package resources.data.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private int page;
    private int count;
    private long total;
    private List<T> members;
}
