package resources.data.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import resources.data.dto.TagDTO;
import resources.data.entity.Tag;

@Component
public class TagDTOConverter implements Converter<TagDTO, Tag> {
    @Override
    public Tag convert(TagDTO source) {
        if (source == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(source.getId());
        tag.setName(source.getName());
        return tag;
    }
}
