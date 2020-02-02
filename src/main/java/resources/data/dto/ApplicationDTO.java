package resources.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    @JsonProperty(access = READ_ONLY)
    private long id;

    private long projectId;

    @JsonProperty(access = READ_ONLY)
    private UserDTO user;

    @NotBlank
    private String description;

    @JsonProperty(access = READ_ONLY)
    private Date date;
}
