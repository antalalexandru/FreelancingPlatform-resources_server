package resources.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Set<TagDTO> tags;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDTO author;

    @JsonProperty(access = READ_ONLY)
    private long enrolled;

    @Min(0)
    private long paymentLowerBound;

    @Min(0)
    private long paymentUpperBound;

    @JsonProperty(access = READ_ONLY)
    private Date submitted;

    @FutureOrPresent
    private Date endDate;

    private ApplicationDTO selectedApplication;
}
