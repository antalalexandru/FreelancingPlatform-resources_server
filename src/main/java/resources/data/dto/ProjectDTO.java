package resources.data.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
@Builder
public class ProjectDTO {
    private long id;
    private String name;
    private String description;
    private Set<String> tags;
    private long authorId;
    private long enrolled;
    private long paymentLowerBound;
    private long paymentUpperBound;
    private Date submitted;
    private Date endDate;
}
