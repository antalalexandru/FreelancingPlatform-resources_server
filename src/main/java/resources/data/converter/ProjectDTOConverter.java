package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.ProjectDTO;
import resources.data.entity.Project;
import resources.data.entity.Tag;

import java.util.stream.Collectors;

@Component
public class ProjectDTOConverter implements Converter<Project, ProjectDTO> {

    @Override
    public ProjectDTO convert(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .tags(project.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .submitted(project.getSubmitted())
                .endDate(project.getEndDate())
                .paymentLowerBound(project.getPaymentLowerBound())
                .paymentUpperBound(project.getPaymentUpperBound())
                .build();
    }
}
