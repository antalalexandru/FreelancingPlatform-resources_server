package resources.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import resources.data.dto.ApplicationDTO;
import resources.data.dto.PaginatedResponse;
import resources.data.dto.ProjectDTO;
import resources.service.ProjectService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@CrossOrigin("*")
@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public PaginatedResponse<ProjectDTO> getProjects(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Valid @Min(1) int page,
            @RequestParam(value = "count", required = false, defaultValue = "20") @Valid @Min(1) @Max(30) int count,
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(value = "sort", required = false, defaultValue = "id:desc") String sort) {

        return projectService.getProjects(page, count, query, sort);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ProjectDTO createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    @GetMapping("{id}")
    public ProjectDTO getProjectDetails(@PathVariable("id") long id) {
        return projectService.getProjectDetails(id);
    }

    @PostMapping("{id}/application")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ApplicationDTO applyToProject(@PathVariable("id") long projectId, @RequestBody @Valid ApplicationDTO applicationDTO) {
        return projectService.applyToProject(projectId, applicationDTO);
    }

    @GetMapping("{id}/application")
    public PaginatedResponse<ApplicationDTO> getProjectApplications(
            @PathVariable("id") long projectId,
            @RequestParam(value = "page", required = false, defaultValue = "1") @Valid @Min(1) int page,
            @RequestParam(value = "count", required = false, defaultValue = "20") @Valid @Min(1) @Max(30) int count) {

        return projectService.getProjectApplications(projectId, page, count);
    }

    @GetMapping("{id}/can_post_application")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public void checkIfAbleToPost(@PathVariable("id") long projectId) {
        projectService.checkIfAbleToApply(projectId);
    }
}
