package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.UserDTO;
import resources.data.entity.User;

@Component
public class UserConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
