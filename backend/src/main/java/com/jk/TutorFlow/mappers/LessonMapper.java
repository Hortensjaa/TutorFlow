package com.jk.TutorFlow.mappers;

import com.jk.TutorFlow.entities.File;
import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.models.LessonModel;
import com.jk.TutorFlow.models.TagModel;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class LessonMapper {
    public LessonModel toModel(Lesson entity) {
        LessonModel model = new LessonModel();
        model.setID(entity.getLesson_id());
        model.setTopic(entity.getTopic());
        model.setDate(entity.getDate().toLocalDate());
        model.setDescription(entity.getDescription());
        Integer rate = entity.getRate();
        model.setRate(rate != null ? rate : 0);
        model.setStudent(entity.getStudent() != null ? entity.getStudent().getName() : "DELETED STUDENT");
        model.setStudentID(entity.getStudent() != null ? entity.getStudent().getStudent_id() : -1);
        model.setTeacher(entity.getTeacher().getUsername());
        model.setFiles(entity.getFiles().stream().map(File::getPath).toArray(String[]::new));
        model.setTags(entity.getTags().stream()
                .map(tag -> {
                    TagModel t = new TagModel();
                    t.setID(tag.getTag_id());
                    t.setName(tag.getName());
                    return t;
                })
                .toArray(TagModel[]::new));

        return model;
    }

    public Lesson toEntity(LessonModel model) {
        Lesson entity = new Lesson();
        entity.setTopic(model.getTopic());
        entity.setDescription(model.getDescription());
        entity.setDate(Date.valueOf(model.getDate()));
        Integer rate = model.getRate();
        entity.setRate(rate != null ? rate : 0);
        return entity;
    }
}
