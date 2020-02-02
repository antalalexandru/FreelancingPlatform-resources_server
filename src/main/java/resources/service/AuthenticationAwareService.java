package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import resources.data.converter.UserConverter;
import resources.data.dto.UserDTO;
import resources.data.repository.UserRepository;

abstract class AuthenticationAwareService {

    private UserRepository userRepository;

    private UserConverter userConverter;

    @Autowired
    public final void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public final void setUserConverter(final UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    UserDTO getAuthenticatedUserDTO() {
        return userConverter.convert(
                userRepository.findByUsernameEquals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                        .orElseThrow(() -> new RuntimeException("Invalid Authentication"))); // TODO
    }
}
