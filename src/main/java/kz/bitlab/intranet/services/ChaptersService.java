package kz.bitlab.intranet.services;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Courses;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface ChaptersService {
    Chapters getChapterById(Long id);
    List<Chapters> getAllChapters(Courses courses, Pageable pageable);
    Chapters getByCourseAndOrder(Courses courses, int order);
    List<Chapters> getAllChaptersToDeleteOrders(Courses courses);
    void deleteChapter(Chapters chapters);
}
