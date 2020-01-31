package resources.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationTokenRequestDTO {
    @NotBlank
    private String token;
    private Long userId;
}
