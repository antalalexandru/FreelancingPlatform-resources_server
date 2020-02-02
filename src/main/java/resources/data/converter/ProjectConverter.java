package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.ProjectDTO;
import resources.data.entity.Project;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProjectConverter implements Converter<Project, ProjectDTO> {

    private final TagConverter tagConverter;
    private final UserConverter userConverter;

    public ProjectConverter(TagConverter tagConverter, UserConverter userConverter) {
        this.tagConverter = tagConverter;
        this.userConverter = userConverter;
    }

    @Override
    public ProjectDTO convert(Project project) {
        if (project == null) {
            return null;
        }
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .tags(project.getTags() == null ? Collections.emptySet() : project.getTags().stream()
                        .map(tagConverter::convert)
                        .collect(Collectors.toSet()))
                .author(userConverter.convert(project.getAuthor()))
                .submitted(project.getSubmitted())
                .endDate(project.getEndDate())
                .paymentLowerBound(project.getPaymentLowerBound())
                .paymentUpperBound(project.getPaymentUpperBound())
                .build();
    }
}
