package resources.data.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import resources.data.dto.ProjectDTO;
import resources.data.dto.UserRequestDTO;
import resources.data.entity.Group;
import resources.data.entity.Project;
import resources.data.entity.Tag;
import resources.data.entity.User;

import java.util.stream.Collectors;

@Component
public class UserRequestDTOToUserConverter implements Converter<UserRequestDTO, User> {
    private static final long DEFAULT_GROUP = 2;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User convert(UserRequestDTO userDTO) {
        return User.builder()
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .group(Group.builder().id(DEFAULT_GROUP).build())
                .build();
    }
}
