package resources.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import resources.data.converter.TagConverter;
import resources.data.dto.TagDTO;
import resources.data.repository.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    public TagService(TagRepository tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    public List<TagDTO> getTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagConverter::convert)
                .collect(Collectors.toList());
    }
}
