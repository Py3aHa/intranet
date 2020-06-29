package kz.bitlab.intranet.services.impl;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Lesson;
import kz.bitlab.intranet.repository.LessonsRepository;
import kz.bitlab.intranet.services.LessonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableWebSecurity
public class LessonServiceImpl implements LessonsService {
    @Autowired
    LessonsRepository lessonsRepository;

    @Override
    public Lesson getLessonById(Long id) {
        return lessonsRepository.getOne(id);
    }

    @Override
    public List<Lesson> getAllLessons(Chapters chapters, Pageable pageable) {
        return lessonsRepository.findAllByChapterOrderByOrder(chapters, pageable);
    }

    @Override
    public Lesson getOneByChapterAndOrder(Chapters chapter, int order) {
        return lessonsRepository.findByChapterAndOrder(chapter, order);
    }

    @Override
    public List<Lesson> getAllLessonsToDeleteOrders(Chapters chapters) {
        return lessonsRepository.findAllByChapter(chapters);
    }


}
