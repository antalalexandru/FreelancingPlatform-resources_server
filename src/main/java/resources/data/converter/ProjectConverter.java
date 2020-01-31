package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.ProjectDTO;
import resources.data.entity.Project;

@Component
public class ProjectConverter implements Converter<ProjectDTO, Project> {

    @Override
    public Project convert(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setDescription(projectDTO.getDescription());
        project.setSubmitted(projectDTO.getSubmitted());
        project.setEndDate(projectDTO.getEndDate());
        project.setAuthorId(projectDTO.getAuthorId());
        return project;
    }
}
