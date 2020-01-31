package resources.data.dto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.Set;

@Data
@Builder
public class ProjectDTO {
    private long id;

    @Size(min = 1, max = 255, message = "The project name must not be longer than 255 characters.")
    private String name;

    private String description;

    private Set<String> tags;


    private long authorId;

    private long enrolled;

    @Min(0)
    private long paymentLowerBound;

    @Min(0)
    private long paymentUpperBound;

    private Date submitted;

    @FutureOrPresent
    private Date endDate;
}
