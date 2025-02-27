package com.jk.TutorFlow.services;

import com.jk.TutorFlow.entities.Lesson;
import com.jk.TutorFlow.entities.Tag;
import com.jk.TutorFlow.entities.User;
import com.jk.TutorFlow.mappers.TagMapper;
import com.jk.TutorFlow.models.TagModel;
import com.jk.TutorFlow.repositories.LessonRepository;
import com.jk.TutorFlow.repositories.TagRepository;
import com.jk.TutorFlow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TagService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;

    public TagModel addNewTag(String tag_name, Long teacher_id) {
        Tag tag = new Tag(tag_name);
        Optional<User> user = userRepository.findById(teacher_id);
        if (user.isPresent()) {
            user.get().getTags().add(tag);
            userRepository.save(user.get());
            tag.setTeacher(user.get());
            tagRepository.save(tag);
        } else {
            throw new RuntimeException("User not found");
        }
        return tagMapper.toModel(tag);
    }

    public TagModel addTagsToLesson(Set<Long> tag_ids, Long lesson_id) {
        Optional<Lesson> lesson = lessonRepository.findById(lesson_id);
        if (lesson.isPresent()) {
            lesson.get().getTags().clear();
            tag_ids.forEach(tag_id -> {
                Tag tag = tagRepository.getReferenceById(tag_id);
                lesson.get().getTags().add(tag);
                tag.getLessons().add(lesson.get());
                tagRepository.save(tag);
            });
            lessonRepository.save(lesson.get());
            return tagMapper.toModel(tagRepository.findById(tag_ids.iterator().next()).get());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<TagModel> getTeachersTags(Long teacher_id) {
        return tagRepository.getTeachersTags(teacher_id).stream().map(tagMapper::toModel).toList();
    }
}
