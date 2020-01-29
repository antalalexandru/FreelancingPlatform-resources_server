package resources.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import resources.data.dto.PaginatedResponse;
import resources.data.dto.ProjectDTO;
import resources.service.ProjectService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public PaginatedResponse<ProjectDTO> getProjects(
            @RequestParam(value = "page", required = false, defaultValue = "0") @Valid @Min(0) int page,
            @RequestParam(value = "count", required = false, defaultValue = "20") @Valid @Min(1) @Max(30) int count,
            @RequestParam(value = "query", required = false, defaultValue = "") String query) {

        return projectService.getProjects(page, count, query);
    }

}
