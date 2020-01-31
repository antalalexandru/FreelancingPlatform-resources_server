package resources.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthenticationRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
