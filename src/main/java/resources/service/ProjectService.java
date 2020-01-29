package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resources.data.converter.ProjectDTOConverter;
import resources.data.dto.PaginatedResponse;
import resources.data.dto.ProjectDTO;
import resources.data.entity.Project;
import resources.data.repository.ProjectRepository;
import resources.data.specitications.ProjectSpecificationBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectDTOConverter projectDTOConverter;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectDTOConverter projectDTOConverter) {
        this.projectRepository = projectRepository;
        this.projectDTOConverter = projectDTOConverter;
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ProjectDTO> getProjects(int pageNumber, int itemsPerPage, String query) {
        List<Project> projects;
        long total;

        if (query.length() > 0) {
            Specification<Project> specification = ProjectSpecificationBuilder.build(query);
            total = projectRepository.count(specification);
            projects = projectRepository.findAll(specification, PageRequest.of(pageNumber, itemsPerPage)).getContent();
        } else {
            total = projectRepository.count();
            projects = projectRepository.findAll(PageRequest.of(pageNumber, itemsPerPage)).getContent();
        }

        List<ProjectDTO> projectDTOList = projects.stream().map(projectDTOConverter::convert).collect(Collectors.toList());

        return PaginatedResponse.<ProjectDTO>builder()
                .page(pageNumber)
                .count(itemsPerPage)
                .total(total)
                .members(projectDTOList)
                .build();
    }
}
