package kz.bitlab.intranet.services.impl;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Courses;
import kz.bitlab.intranet.entities.courses.Lesson;
import kz.bitlab.intranet.repository.ChaptersRepository;
import kz.bitlab.intranet.repository.CoursesRepository;
import kz.bitlab.intranet.repository.LessonsRepository;
import kz.bitlab.intranet.services.ChaptersService;
import kz.bitlab.intranet.services.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@EnableWebSecurity
public class CoursesServiceImpl implements CoursesService {
    @Autowired
    CoursesRepository coursesRepository;

    @Autowired
    ChaptersRepository chaptersRepository;

    @Autowired
    ChaptersService chaptersService;

    @Override
    public Courses getOneById(Long id) {
        Courses course = coursesRepository.getOne(id);
        return course;
    }

    @Override
    public Courses getOneByName(String name) {
        return coursesRepository.findByName(name);
    }

    @Override
    public List<Courses> getAllCourses(Pageable pageable) {
        List<Courses> coursesList = coursesRepository.findAllBy(pageable);
        return coursesList;
    }

    @Override
    public void deleteCourse(Courses courses) {
        List<Chapters> chaptersList = chaptersRepository.findAllByCourse(courses);
        for(Chapters c: chaptersList){
            chaptersService.deleteChapter(c);
        }

        coursesRepository.delete(courses);
    }
}
