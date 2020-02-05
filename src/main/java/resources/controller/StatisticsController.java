package resources.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import resources.data.dto.TagsStatisticsDTO;
import resources.data.repository.TagRepository;
import resources.service.StatisticsService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/tags")
    public List<TagsStatisticsDTO> getTagsStatistics() {
        return statisticsService.getTagsStatistics();
    }

    @GetMapping("/users")
    public long getUsersCount() {
        return statisticsService.getUsersCount();
    }

    @GetMapping("/projects")
    public long getProjectsCount() {
        return statisticsService.getProjectsCount();
    }
}
