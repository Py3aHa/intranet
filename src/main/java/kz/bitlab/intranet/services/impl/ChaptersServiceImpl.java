package kz.bitlab.intranet.services.impl;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Courses;
import kz.bitlab.intranet.entities.courses.Lesson;
import kz.bitlab.intranet.repository.ChaptersRepository;
import kz.bitlab.intranet.repository.LessonsRepository;
import kz.bitlab.intranet.services.ChaptersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableWebSecurity
public class ChaptersServiceImpl implements ChaptersService {
    @Autowired
    ChaptersRepository chaptersRepository;

    @Autowired
    LessonsRepository lessonsRepository;

    @Override
    public Chapters getChapterById(Long id) {
        return chaptersRepository.getOne(id);
    }

    @Override
    public List<Chapters> getAllChapters(Courses courses, Pageable pageable) {
        return chaptersRepository.findAllByCourseOrderByOrder(courses, pageable);
    }

    @Override
    public Chapters getByCourseAndOrder(Courses courses, int order) {
        return chaptersRepository.findAllByCourseAndOrder(courses, order);
    }

    @Override
    public List<Chapters> getAllChaptersToDeleteOrders(Courses courses) {
        return chaptersRepository.findAllByCourseOrderByOrder(courses);
    }

    @Override
    public void deleteChapter(Chapters chapters) {
        List<Lesson> lessonList = lessonsRepository.findAllByChapter(chapters);
        for(Lesson l: lessonList){
            lessonsRepository.delete(l);
        }

        chaptersRepository.delete(chapters);
    }
}
