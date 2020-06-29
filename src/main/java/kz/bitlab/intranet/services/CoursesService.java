package kz.bitlab.intranet.services;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Courses;
import kz.bitlab.intranet.entities.courses.Lesson;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CoursesService {
    Courses getOneById(Long id);
    Courses getOneByName(String name);
    List<Courses> getAllCourses(Pageable pageable);
    void deleteCourse(Courses courses);
}
