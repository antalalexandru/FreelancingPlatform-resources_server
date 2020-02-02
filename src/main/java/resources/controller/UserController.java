package resources.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import resources.data.dto.*;
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

    @GetMapping("/forgotPassword")
    public void forgotPassword(@RequestParam("keyword") String keyword) {
        userService.forgotPassword(keyword);
    }

    @PostMapping("/reset")
    public void resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        userService.resetPassword(request);
    }

}
