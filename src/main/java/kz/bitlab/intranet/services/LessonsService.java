package kz.bitlab.intranet.services;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Lesson;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonsService {
    Lesson getLessonById(Long id);
    List<Lesson> getAllLessons(Chapters chapters, Pageable pageable);
    Lesson getOneByChapterAndOrder(Chapters chapter, int order);
    List<Lesson> getAllLessonsToDeleteOrders(Chapters chapters);
}
