package resources.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.dto.ActivationTokenRequestDTO;
import resources.data.dto.AuthenticationRequestDTO;
import resources.data.dto.AuthenticationResponseDTO;
import resources.data.dto.UserRequestDTO;
import resources.service.UserService;

import javax.validation.Valid;

@CrossOrigin("*")
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

    @PostMapping
    public void register(@RequestBody @Valid UserRequestDTO user) {
        userService.register(user);
    }

    @PostMapping("/activate")
    public void activate(@RequestBody @Valid ActivationTokenRequestDTO activationTokenRequestDTO) {
        userService.activate(activationTokenRequestDTO);
    }
}
