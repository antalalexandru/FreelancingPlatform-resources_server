package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.TagDTO;
import resources.data.entity.Tag;

@Component
public class TagConverter implements Converter<Tag, TagDTO> {
    @Override
    public TagDTO convert(Tag source) {
        if (source == null) {
            return null;
        }
        return new TagDTO(source.getId(), source.getName());
    }
}
