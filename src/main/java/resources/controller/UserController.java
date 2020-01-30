package resources.controller;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.dto.AuthenticationRequestDTO;
import resources.data.dto.AuthenticationResponseDTO;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@Valid AuthenticationRequestDTO authenticationRequest) {
        return null;
    }

    @PostMapping("/signup")
    public void signup() {

    }

}
