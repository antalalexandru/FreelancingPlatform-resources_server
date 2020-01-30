package resources.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.dto.AuthenticationRequestDTO;
import resources.data.dto.AuthenticationResponseDTO;
import resources.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthenticationResponseDTO login(@RequestBody @Valid AuthenticationRequestDTO authenticationRequest) {
        return userService.handleAuthenticationRequest(authenticationRequest);
    }

    @PostMapping("/signup")
    public void signup() {

    }

}
