package resources.data.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.ApplicationDTO;
import resources.data.entity.Application;

@Component
public class ApplicationDTOConverter implements Converter<ApplicationDTO, Application> {

    private final UserDTOConverter userDTOConverter;

    public ApplicationDTOConverter(UserDTOConverter userDTOConverter) {
        this.userDTOConverter = userDTOConverter;
    }

    @Override
    public Application convert(ApplicationDTO applicationDTO) {
        if (applicationDTO == null) {
            return null;
        }
        return Application.builder()
                .id(applicationDTO.getId())
                .user(userDTOConverter.convert(applicationDTO.getUser()))
                .description(applicationDTO.getDescription())
                .projectId(applicationDTO.getProjectId())
                .date(applicationDTO.getDate())
                .build();
    }
}
