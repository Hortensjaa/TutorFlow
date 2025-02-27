package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.Tag;
import com.jk.TutorFlow.models.TagModel;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public TagModel toModel(Tag entity) {
        TagModel model = new TagModel();
        model.setID(entity.getTag_id());
        model.setName(entity.getName());
        return model;
    }

    public Tag toEntity(TagModel model) {
        Tag entity = new Tag();
        entity.setTag_id(model.getID());
        entity.setName(model.getName());
        return entity;
    }
}
