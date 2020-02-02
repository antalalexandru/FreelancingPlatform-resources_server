package resources.data.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.ApplicationDTO;
import resources.data.entity.Application;

@Component
public class ApplicationConverter implements Converter<Application, ApplicationDTO> {

    private final UserConverter userConverter;

    public ApplicationConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ApplicationDTO convert(Application application) {
        if (application == null) {
            return null;
        }
        return ApplicationDTO.builder()
                .id(application.getId())
                .projectId(application.getProjectId())
                .user(userConverter.convert(application.getUser()))
                .description(application.getDescription())
                .date(application.getDate())
                .build();
    }
}
