package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.ProjectDTO;
import resources.data.entity.Project;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProjectDTOConverter implements Converter<ProjectDTO, Project> {

    private final UserDTOConverter userDTOConverter;
    private final TagDTOConverter tagDTOConverter;

    public ProjectDTOConverter(UserDTOConverter userDTOConverter, TagDTOConverter tagDTOConverter) {
        this.userDTOConverter = userDTOConverter;
        this.tagDTOConverter = tagDTOConverter;
    }

    @Override
    public Project convert(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        }
        return Project.builder()
                .id(projectDTO.getId())
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .submitted(projectDTO.getSubmitted())
                .endDate(projectDTO.getEndDate())
                .enrolled(projectDTO.getEnrolled())
                .author(userDTOConverter.convert(projectDTO.getAuthor()))
                .tags(projectDTO.getTags() == null ? Collections.emptySet() : projectDTO.getTags().stream().map(tagDTOConverter::convert).collect(Collectors.toSet()))
                .paymentLowerBound(projectDTO.getPaymentLowerBound())
                .paymentUpperBound(projectDTO.getPaymentUpperBound())
                .build();
    }
}
