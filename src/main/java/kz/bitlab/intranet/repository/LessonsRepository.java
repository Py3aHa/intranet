package kz.bitlab.intranet.repository;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonsRepository extends JpaRepository<Lesson, Long> {
    int countAllByChapter(Chapters chapters);
    Optional<Lesson> findById(Long id);
    List<Lesson> findAllByChapterOrderByOrder(Chapters chapters, Pageable pageable);
    Lesson findByChapterAndOrder(Chapters chapter, int order);
    List<Lesson> findAllByChapter(Chapters chapters);
}
