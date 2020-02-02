package resources.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import resources.data.converter.ApplicationConverter;
import resources.data.converter.ApplicationDTOConverter;
import resources.data.converter.ProjectConverter;
import resources.data.converter.ProjectDTOConverter;
import resources.data.dto.ApplicationDTO;
import resources.data.dto.PaginatedResponse;
import resources.data.dto.ProjectDTO;
import resources.data.entity.Application;
import resources.data.entity.Project;
import resources.data.repository.ApplicationRepository;
import resources.data.repository.ProjectRepository;
import resources.data.specitications.ProjectSpecificationBuilder;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService extends AuthenticationAwareService {

    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final ProjectDTOConverter projectDTOConverter;
    private final ApplicationRepository applicationRepository;
    private final ApplicationConverter applicationConverter;
    private final ApplicationDTOConverter applicationDTOConverter;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProjectConverter projectConverter, ProjectDTOConverter projectDTOConverter, ApplicationRepository applicationRepository, ApplicationConverter applicationConverter, ApplicationDTOConverter applicationDTOConverter) {
        this.projectRepository = projectRepository;
        this.projectConverter = projectConverter;
        this.projectDTOConverter = projectDTOConverter;
        this.applicationRepository = applicationRepository;
        this.applicationConverter = applicationConverter;
        this.applicationDTOConverter = applicationDTOConverter;
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ProjectDTO> getProjects(int pageNumber, int itemsPerPage, String query, String sort) {
        List<Project> projects;
        long total;
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, itemsPerPage, ServiceUtil.processSortRequest(sort));

        if (query.length() > 0) {
            Specification<Project> specification = ProjectSpecificationBuilder.build(query);
            total = projectRepository.count(specification);
            projects = projectRepository.findAll(specification, pageRequest).getContent();
        } else {
            total = projectRepository.count();
            projects = projectRepository.findAll(pageRequest).getContent();
        }

        List<ProjectDTO> projectDTOList = projects.stream().map(projectConverter::convert).collect(Collectors.toList());

        return PaginatedResponse.<ProjectDTO>builder()
                .page(pageNumber)
                .count(projectDTOList.size())
                .total(total)
                .members(projectDTOList)
                .build();
    }

    @Transactional
    public ProjectDTO createProject(final ProjectDTO projectDTO) {
        projectDTO.setAuthor(getAuthenticatedUserDTO());
        projectDTO.setSubmitted(new Date(Calendar.getInstance().getTimeInMillis()));
        Project savedProject = projectRepository.save(projectDTOConverter.convert(projectDTO));
        return projectConverter.convert(savedProject);
    }

    @Transactional(readOnly = true)
    public ProjectDTO getProjectDetails(long projectId) {
        Project project = projectRepository.findByIdEquals(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!")); // TODO
        return projectConverter.convert(project);
    }

    @Transactional
    public ApplicationDTO applyToProject(long projectId, ApplicationDTO applicationDTO) {
        // check if already applied, or do the combined unique key do its job !
        applicationDTO.setDate(new Date());
        applicationDTO.setUser(getAuthenticatedUserDTO());
        applicationDTO.setProjectId(projectId);

        Project project = projectRepository.findByIdEquals(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found!")); // TODO
        if (project.getSelectedApplication() != null) {
            throw new RuntimeException("Project has already a choosed application!"); // TODO Bad request exception
        }

        if (project.getEndDate().before(new Date())) {
            throw new RuntimeException("Project closed!");
        }

        if (project.getAuthor().getId() == applicationDTO.getUser().getId()) {
            throw new RuntimeException("You cannot apply to your own projects o.O!");
        }

        Application savedApplication = applicationRepository.save(applicationDTOConverter.convert(applicationDTO));
        return applicationConverter.convert(savedApplication);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<ApplicationDTO> getProjectApplications(long projectId, int page, int count) {
        // TODO actually do sort by date desc
        List<ApplicationDTO> applications = applicationRepository
                .findByProjectIdEqualsOrderByDateDesc(projectId, PageRequest.of(page - 1, count))
                .stream()
                .map(applicationConverter::convert)
                .collect(Collectors.toList());

        return PaginatedResponse.<ApplicationDTO>builder()
                .page(page)
                .count(applications.size())
                .total(applicationRepository.countByProjectIdEquals(projectId))
                .members(applications)
                .build();
    }
}
