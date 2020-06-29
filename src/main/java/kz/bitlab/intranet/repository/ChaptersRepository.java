package kz.bitlab.intranet.repository;

import kz.bitlab.intranet.entities.courses.Chapters;
import kz.bitlab.intranet.entities.courses.Courses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChaptersRepository extends JpaRepository<Chapters, Long> {
    Optional<Chapters> findById(Long id);
    List<Chapters> findAllByCourseOrderByOrder(Courses courses, Pageable pageable);
    Chapters findAllByCourseAndOrder(Courses courses, int order);
    //Chapters findAllByCourse(Courses courses);
    List<Chapters> findAllByCourseOrderByOrder(Courses courses);
    List<Chapters> findAllByCourse(Courses courses);
    int countAllByCourse(Courses courses);
}
