package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.UserDTO;
import resources.data.entity.User;

@Component
public class UserDTOConverter implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .build();
    }
}
