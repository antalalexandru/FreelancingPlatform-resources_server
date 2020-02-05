package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resources.data.dto.TagsStatisticsDTO;
import resources.data.repository.ProjectRepository;
import resources.data.repository.TagRepository;
import resources.data.repository.UserRepository;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<TagsStatisticsDTO> getTagsStatistics() {
        return tagRepository.getTagsStatistics();
    }

    public long getProjectsCount() {
        return projectRepository.count();
    }

    public long getUsersCount() {
        return userRepository.count();
    }
}
