package kz.bitlab.intranet.repository;

import kz.bitlab.intranet.entities.courses.Courses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesRepository extends JpaRepository<Courses, Long> {
    Optional<Courses> findById(Long id);
    List<Courses> findAllBy(Pageable pageable);
    Courses findByName(String name);
}
